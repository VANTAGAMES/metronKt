import korlibs.datastructure.iterators.*
import korlibs.event.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.ColorUtil.hex
import kotlin.math.*

fun State.verdict() {
    val debug = container.text("", textSize = 20f, color = ColorPalette.text.hex())
    container.keys { justDown(Key.SPACE) { audit(debug) } }
    container.onClick { audit(debug) }
}

fun State.audit(debug: Text) {
    note.alives.fastForEach { ghost ->
        val sub = note.stickAngle.elapsed - ghost.note
        val distance = abs(sub.seconds) - bpmToSec/2
        debug.text = "$distance"
        Audit.values().fastForEach { audit ->
            if (distance in audit.range) {
                spawnAudit(ghost.stick, audit)
                return
            }
        }
        return
    }
}

@Suppress("unused")
enum class Audit(val text: String, val color: RGBA, val range: ClosedFloatingPointRange<Double>) {
    TOO_SLOW("Too Slow!", Colors.RED, 0.5..2.0),
    TOO_FAST("Too Fast!", Colors.RED, -2.0..-0.5),
    SLOW("Slow!", Colors.YELLOW, 0.15..0.5),
    FAST("Fast!", Colors.YELLOW, -0.5..-0.15),
    PERF("Perfect!", Colors.GREEN, -0.15..0.15),
}
fun State.spawnAudit(view: View, audit: Audit) {
    val angle = view.rotation
    val offset = (-90).degrees
    view.removeFromParent()
    note.alives.fastIterateRemove { it.stick == view }
    container.container {
        filter = IdentityFilter
        text(audit.text, 40f, color = audit.color) {
            alignment = TextAlignment.CENTER
            pos = view.pos
            pos = Point(pos.x + stickHeight * cos(angle + offset), pos.y + stickHeight * sin(angle + offset))
            var elapsed = 0.milliseconds
            onEvent(UpdateEvent) {
                elapsed += it.deltaTime
                zIndex = 2f
                if (elapsed.seconds >= 0.2) {
                    removeFromParent()
                }
            }
        }
    }.apply {
//        auditShowEffect {
//            auditHideEffect { removeFromParent() }
//        }
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