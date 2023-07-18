import event.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import util.ColorUtil.hex
import kotlin.coroutines.*
import kotlin.math.*

fun State.verdict() {
    container.onEvent(HitEvent) {
        if (note.stickAngle.elapsed < 0.seconds) return@onEvent
        audit(it.delta)
    }
}

fun State.audit(delta: TimeSpan, debug: Text? = null) {
    note.alives.fastForEach { ghost ->
        val sub = note.stickAngle.elapsed+delta - ghost.note
        val distance = abs(sub.seconds)
//        debug.text = "$distance"
        run {
            Audit.values().fastForEach { audit ->
                if (sub.seconds in audit.range) {
                    hitSound.playNoCancel()
                    spawnAudit(ghost.stick, audit)
                    screenContainer.dispatch(AuditEvent(ghost, audit))
                    return@run
                }
            }
        }
    }
}

@Suppress("unused")
enum class Audit(val text: String, val color: RGBA, val range: ClosedFloatingPointRange<Double>) {

    TOO_FAST("너무 빠름!", Colors.RED, -2.0..-1.0),
    TOO_SLOW("너무 느림!", Colors.RED, 1.0..2.0),
    FAST("빠름!", Colors.YELLOW, -1.0..-0.05),
    SLOW("느림!", Colors.YELLOW, 0.05..1.0),
    PERF("정확!", Colors.GREEN, -0.05..0.05),
}
fun State.spawnAudit(view: View, audit: Audit) {
    val angle = view.rotation
    val offset = (-90).degrees
    view.noteHitEffect {
        view.removeFromParent()
    }
    note.alives.fastIterateRemove { it.stick == view }
    container.container {
        filter = IdentityFilter
        text(" ${audit.text} ", 30f, color = audit.color, font = boldFont) {
            filter = BlurFilter(0.5f)
            alignment = TextAlignment.CENTER
            pos = view.pos
            val auditHeight = stickHeight + 50
            pos = Point(pos.x + auditHeight * cos(angle + offset), pos.y + auditHeight * sin(angle + offset))
            var elapsed = 0.milliseconds
            onEvent(UpdateEvent) {
                elapsed += it.deltaTime
                zIndex = 2f
                if (elapsed.seconds >= 0.15) {
                    removeFromParent()
                }
            }
        }
    }
}

fun View.auditHideEffect(period: TimeSpan = 1.seconds, easing: Easing = Easing.EASE_OUT, callback: () -> Unit) {
    val startTime = DateTime.now()
    zIndex = 10f
    onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            callback()
        } else {
            val i = (span / period)
            alpha = min(1f, max(0f, easing.invoke(i)))
        }
    }
}

fun View.auditShowEffect(period: TimeSpan = 1.seconds, easing: Easing = Easing.EASE_OUT, callback: () -> Unit) {
    val startTime = DateTime.now()
    zIndex = 10f
    onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            callback()
        } else {
            val i = (span / period)
            alpha = 1 - min(1f, max(0f, easing.invoke(i)))
        }
    }
}
