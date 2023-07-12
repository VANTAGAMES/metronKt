import event.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.io.async.*
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
    val debug = container.text("", textSize = 20f, color = ColorPalette.text.hex())
    container.onEvent(HitEvent) {
        audit(it.delta, debug)
    }
}

fun State.audit(delta: TimeSpan, debug: Text?) {
    hitSound.playNoCancel()
    note.alives.fastForEach { ghost ->
        val sub = note.stickAngle.elapsed+delta - ghost.note
        val distance = abs(sub.seconds) - bpmToSec / 2
//        debug.text = "$distance"
        run {
            Audit.values().fastForEach { audit ->
                if (distance in audit.range) {
                    spawnAudit(ghost.stick, audit)
                    container.dispatch(AuditEvent(ghost, audit))
                    return@run
                }
            }
        }
    }
}

@Suppress("unused")
enum class Audit(val text: String, val color: RGBA, val range: ClosedFloatingPointRange<Double>) {
    PERF("정확!", Colors.GREEN, -0.05..0.05),
    FAST("빠름!", Colors.YELLOW, -1.0..0.05),
    SLOW("느림!", Colors.YELLOW, -0.05..1.0),
    TOO_FAST("너무 빠름!", Colors.RED, -2.0..-1.0),
    TOO_SLOW("너무 느림!", Colors.RED, 1.0..2.0),
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
