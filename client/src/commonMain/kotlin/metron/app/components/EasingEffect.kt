package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*
import korlibs.event.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.util.*
import util.*

class EasingEffect(
    val easing: Easing,
    val effects: Array<out Effect>,
    val finishing: View.() -> Unit,
    val timeSpan: TimeSpan,
    val isDown: Boolean,
    val view: View
) {
    lateinit var onDestroyView: Cancellable

    fun enableEffect() {
        val span = timeSpan
        startEffect()
        screen.schedule(span,
            onDelay = { elapsed ->
                val period = if (isDown) 1 - (elapsed / span) else elapsed / span
                effects.fastForEach { it.applyEffect(view, easing(period)) }
            }, onStopped = {
                stopEffect()
            }
        )
    }

    fun startEffect() {
        val newParent = view.parent?.container()?: return
        onDestroyView = view.onEvent(DestroyEvent) { newParent.removeFromParent() }
        view.removeFromParent()
        view.addTo(newParent)
    }

    fun stopEffect() {
        val originalParent = view.parent?.parent?: view.parent?: return
        view.parent?.removeFromParent()
        view.removeFromParent()
        view.addTo(originalParent)
        onDestroyView.cancel()
        finishing(view)
    }


}

fun interface Effect {
    fun applyEffect(view: View, value: Float)

    companion object {
        fun View.easingEffect(
            timeSpan: TimeSpan,
            easing: Easing = Easing.EASE_OUT,
            isDown: Boolean = false,
            effects: Array<Effect>,
            finishing: View.() -> Unit = {},
        ) = EasingEffect(
            easing = easing,
            finishing = finishing,
            timeSpan = timeSpan,
            isDown = isDown,
            effects = effects,
            view = this
        ).enableEffect()
        fun posX(magnanimity: Float): Effect {
            var origin: Float? = null
            return Effect { view, value ->
                if (origin === null) origin = view.pos.x
                view.positionX(origin!! + magnanimity * value)
            }
        }
        fun posY(magnanimity: Float): Effect {
            var origin: Float? = null
            return Effect { view, value ->
                if (origin === null) origin = view.pos.y
                view.positionY(origin!! + magnanimity * value)
            }
        }
        fun alpha(magnanimity: Float) = Effect { view, value ->
            view.alpha(magnanimity * value)
        }
    }

}


