package metron.app.components

import com.github.quillraven.fleks.*
import event.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.io.lang.*
import korlibs.korge.time.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.app.handler.*
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.easingEffect
import metron.util.*
import util.*

data class GhostStick(
    val stage: Stage,
    val body: View,
    val angle: Angle,
    val startTime: DateTime,
    val lifeTime: TimeSpan,
    val nextNote: TimeSpan,
    var isHitted: Boolean = false,
) : Component<GhostStick> {
    override fun type() = Companion
    companion object : ComponentHooks<GhostStick>() {
        override val onAdded: ComponentHook<GhostStick> = { entity, ghostStick ->
            screen.timers.timeout(ghostStick.lifeTime) {
                if (ghostStick.isHitted) return@timeout
                ghostStick.body.easingEffect(
                    ghostStick.lifeTime/2, Easing.EASE_OUT,
                    effects = arrayOf(effectAlpha(0.5f, isDown = true))
                ) {
                    screen.dispatch(GhostDrawedEvent(ghostStick, isNaturally = true))
                    removeFromParent()
                    ghostStick.stage.lives.fastIterateRemove { it.body == ghostStick.body }
                    entity.remove()
                }
            }
        }

        operator fun invoke(
            stage: Stage,
            angle: Angle,
            lifeTime: TimeSpan,
            nextNote: TimeSpan,
            startTime: DateTime = DateTime.now()
        ): GhostStick {
            val body = stage.ghostContainer.fastRoundRect(
                corners = RectCorners(1),
                size = stickSize, color = Colors.WHITESMOKE
            ) {
                alpha = 0.5f
                transform { size(stickSize) }
                configurePositionAndAnchor(this)
                rotation = angle
                zIndex = 0f
                easingEffect(stage.bpmToSec.seconds/4, Easing.EASE, arrayOf(
                    effectAlpha(0.5f)
                ))
            }
            return GhostStick(
                stage = stage,
                body = body,
                angle = angle,
                lifeTime = lifeTime,
                nextNote = nextNote,
                startTime = startTime
            )
        }
    }
}

fun View.noteHitEffect(
    stage: Stage, period: TimeSpan = stage.bpmToSec.seconds/6, easing: Easing = Easing.EASE_OUT_QUAD, callback: () -> Unit
) {
    val startTime = DateTime.now()
    zIndex = 0f
    lateinit var listener: Cancellable
    listener = onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            listener.cancel()
            callback()
        } else {
            val i = (1 - (span / period))
            val a = easing.invoke(i)
            scale(1 + a, 1f)
        }
    }
}

