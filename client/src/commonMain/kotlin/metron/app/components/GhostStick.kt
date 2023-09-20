package metron.app.components

import com.github.quillraven.fleks.*
import event.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.io.lang.*
import korlibs.korge.time.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.app.components.Effect.Companion.alpha
import metron.app.components.Effect.Companion.easingEffect
import metron.util.*
import util.*
import kotlin.math.*

val stickHeight get() = screen.height*2/3
val stickWidth get() = sqrt(stickHeight).pow(0.9f)
val stickSize get() = Size(stickWidth, stickHeight)

data class GhostStick(
    val stage: Stage,
    val body: View,
    val angle: Angle,
    val startTime: DateTime,
    val lifeTime: TimeSpan,
    val nextNote: TimeSpan,
) : Component<GhostStick> {
    override fun type() = Companion
    companion object : ComponentHooks<GhostStick>() {
        fun configurePosition(container: View) = container.transform {
            if (this is Anchorable) anchor(0.5f, 1 - (width/2) / height)
            centerXOn(screen)
            positionY(screen.height*7/8)
        }

        override val onAdded: ComponentHook<GhostStick> = { entity, ghostStick ->
            screen.timers.timeout(ghostStick.lifeTime) {
                ghostStick.body.easingEffect(
                    0.15.seconds, Easing.EASE_OUT,
                    isDown = true, effects = arrayOf(alpha(1f))
                ) {
                    removeFromParent()
                    ghostStick.stage.alives.fastIterateRemove { it.body == ghostStick.body }
                    entity.remove()
                }
            }
        }

        operator fun invoke(
            stage: Stage,
            spawner: GhostStickSpawner,
            angle: Angle,
            lifeTime: TimeSpan,
            nextNote: TimeSpan,
            startTime: DateTime = DateTime.now()
        ): GhostStick {
            val body = spawner.ghostContainer.fastRoundRect(
                corners = RectCorners(1),
                size = stickSize, color = Colors["9DB2BF"]
            ) {
                transform { size(stickSize) }
                configurePosition(this)
                rotation = angle
                zIndex = 0f
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
    period: TimeSpan = 0.2.seconds, easing: Easing = Easing.EASE_OUT_QUAD, callback: () -> Unit
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
            val i = (1 - (span / period))/15
            val a = min(1f, max(0f, easing.invoke(i)))
            scale(1 + a, 1 + a/12)
        }
    }
}

fun View.noteDisappearEffect(period: TimeSpan = 0.15.seconds, easing: Easing = Easing.EASE_OUT, callback: View.() -> Unit) {
    val startTime = DateTime.now()
    zIndex = 0f
    onEvent(ViewsUpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            callback()
        } else {
            val i = (1 - (span / period))
            alpha = min(1f, max(0f, easing.invoke(i)))
        }
    }
}

