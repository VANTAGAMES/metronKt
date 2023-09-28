package metron.util

import korlibs.datastructure.*
import korlibs.datastructure.iterators.*
import korlibs.event.*
import korlibs.io.lang.*
import korlibs.io.util.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import metron.*
import metron.util.Effect.Companion.EffectComponentKey
import util.*

class EasingEffect(
    val easing: Easing,
    val effects: Array<out Effect>,
    val finishing: View.() -> Unit,
    val timeSpan: TimeSpan,
    val view: View,
    val interval: (TimeSpan) -> TimeSpan
) {
    private lateinit var onDestroyView: Cancellable
    private var scheduledTask: Cancellable? = null

    fun enableEffect() {
        val span = timeSpan
        startEffect()
        var elapsed = 0.seconds
        scheduledTask = view.addUpdater {
            elapsed += interval(it)
            if (elapsed > span) {
                scheduledTask?.cancel()
                stopEffect()
            }
            effects.fastForEach { it.applyEffect(view, easing(elapsed / span)) }
        }
    }

    fun startEffect() {
        view.parent?.apply {
            if (hasExtra(EffectComponentKey)) {
                getExtraTyped<EasingEffect>(EffectComponentKey)!!.stopEffect()
            }
        }
        val originalParent = view.parent?: return
        val newParent = originalParent.container()
        val easingEffect = this
        newParent.setExtra(EffectComponentKey, easingEffect)

        onDestroyView = newParent.onEvent(DestroyEvent) { newParent.removeFromParent() }
        view.removeFromParent()
        view.addTo(newParent)
    }

    fun stopEffect() {
        val tuckedParent = view.parent
        val thisEasingEffect = this
        scheduledTask?.cancel()
        tuckedParent?.apply {
            if (hasExtra(EffectComponentKey)) {
                if (getExtraTyped<EasingEffect>(EffectComponentKey) == thisEasingEffect) {
                    extra?.remove(EffectComponentKey)
                }
            }
        }
        val originalParent = (view.parent?: run {
            onDestroyView.cancel()
            finishing(view)
            return
        }).parent!!
        tuckedParent?.removeFromParent()
        view.removeFromParent()
        view.addTo(originalParent)
        onDestroyView.cancel()
        finishing(view)
    }


}

fun interface Effect {
    fun applyEffect(view: View, value: Float)

    companion object {
        val EffectComponentKey = UUID.randomUUID().toString()
        val EffectedPosY = UUID.randomUUID().toString()
        val EffectedPosX = UUID.randomUUID().toString()
        inline fun View.easingEffect(
            timeSpan: TimeSpan,
            easing: Easing = Easing.EASE_OUT,
            effects: Array<Effect>,
            noinline interval: (TimeSpan) -> TimeSpan = { it },
            noinline finishing: View.() -> Unit = {},
        ) = EasingEffect(
            easing = easing,
            finishing = finishing,
            timeSpan = timeSpan,
            effects = effects,
            view = this,
            interval = interval
        ).enableEffect()
        fun effectPosX(magnanimity: Float, isDown: Boolean = false): Effect {
            var origin: Float? = null
            return Effect { view, value ->
                if (origin === null) origin = view.pos.x
                view.positionX(origin!! + magnanimity * if (isDown) 1 - value else value)
                view.setExtra(EffectedPosX, view.pos.x/screen.width)
            }
        }
        fun effectPosY(magnanimity: Float, isDown: Boolean = false): Effect {
            var origin: Float? = null
            return Effect { view, value ->
                if (origin === null) origin = view.pos.y
                view.positionY(origin!! + magnanimity * if (isDown) 1 - value else value)
                view.setExtra(EffectedPosY, view.pos.y/screen.height)
            }
        }
        fun effectAlpha(magnanimity: Float, isDown: Boolean = false) = Effect { view, value ->
            view.alpha(magnanimity * if (isDown) 1 - value else value)
        }
    }

}


