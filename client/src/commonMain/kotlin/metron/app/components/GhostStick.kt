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
import metron.util.*
import util.*

const val stickHeight = 480
const val stickWidth = 12

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
                ghostStick.body.noteDisappearEffect {
                    screen.dispatch(
                        GhostDrawedEvent(ghostStick, isNaturally = true)
                    )
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
                    size = Size(stickWidth, stickHeight), color = Colors.LIMEGREEN
                ) {
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
            val a = kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
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
            alpha = kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
        }
    }
}

