import event.*
import korlibs.datastructure.iterators.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.*
import util.ColorUtil.hex
import kotlin.math.*

class LivingGhost(
    val state: State,
    val angle: Angle,
    val lifeTime: TimeSpan,
    val note: TimeSpan,
) {
    val startTime: DateTime = DateTime.now()
    lateinit var stick: Stick
    init {
        state.spawnGhost()
    }
    private fun State.spawnGhost(): Stick = note.run {
        Container().addTo(container) {
            filter = IdentityFilter
            stick = note(ColorPalette.ghost.hex()) {
                rotation = angle
                zIndex = 0f
            }
            var elapsed = 0.milliseconds
            lateinit var cancellable: Cancellable
            cancellable = onEvent(UpdateEvent) {
                elapsed += it.deltaTime
                if (elapsed > lifeTime * bpmToSec / 2) {
                    cancellable.cancel()
                    noteDisappearEffect {
                        this@spawnGhost.container.dispatch(
                            GhostDrawedEvent(this@LivingGhost, isNaturally = true)
                        )
                        removeFromParent()
                        alives.fastIterateRemove { ghost -> ghost.stick == stick }
                    }
                }
            }

        }
        stick
    }
}

fun State.ghostSpawner() {
    note.view.onEvent(UpdateEvent) {
        note.run {
            ghostStick.update(it.deltaTime)
            if (iter.hasNext()) {
                val nextSec = curr.seconds * bpmToSec
                val prevSec = prev.seconds * bpmToSec
                if (ghostStick.elapsed <= 0.seconds) return@onEvent
                val prevAngle = ghostStick.performAngle(prevSec)
                val angle = ghostStick.performAngle(nextSec)
//            println("note=$angle, stick=${stick.performAngle().degrees}")
                val distance = angle.absBetween180degrees(stickAngle.performAngle())
                val length = prevAngle.absBetween180degrees(angle)
//                println("distance=$distance, length=$length")
                if (distance <= length / 2) {
                    val lifeTime = bpmToSec.seconds
                    val ghost = LivingGhost(state, angle, lifeTime, prevSec)
                    alives.add(ghost)
                    state.container.dispatch(GhostSpawnEvent(angle, lifeTime, ghost))
                    prev = curr
                    curr += iter.next()
                    count++
                }
            }
        }
    }
}

fun View.noteHitEffect(period: TimeSpan = 0.2.seconds, easing: Easing = Easing.EASE_OUT_QUAD, callback: () -> Unit) {
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

fun Container.noteDisappearEffect(period: TimeSpan = 0.15.seconds, easing: Easing = Easing.EASE_OUT, callback: () -> Unit) {
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
