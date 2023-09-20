package metron.app.components

import com.github.quillraven.fleks.*
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
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.easingEffect
import metron.util.*
import util.*
import kotlin.math.*

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
        override val onAdded: ComponentHook<GhostStick> = { entity, ghostStick ->
            screen.timers.timeout(ghostStick.lifeTime) {
                ghostStick.body.easingEffect(
                    0.15.seconds, Easing.EASE_OUT,
                    effects = arrayOf(effectAlpha(1f, isDown = true))
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
