import korlibs.datastructure.iterators.*
import korlibs.event.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.io.file.std.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.serialization.json.*
import util.ColorUtil.hex
import kotlin.math.*

//9BA4B5
//212A3E
//394867
//F1F6F9

var magnanimity = 0.8
val myEasing = Easing { (((cos(PI*it)+1) / 2)*magnanimity - 0.5*magnanimity).toFloat() + 0.5f }
typealias Stick = FastRoundRect

class State(
    val level: Level,
    val degrees: Angle,
    val easing: Easing,
    val container: Container,
) {
    val bpm get() = level.bpm
    val note: Note = note(StickAngle(degrees, bpm, easing, offset = 1.seconds))
    val bpmToSec get() = 60.0 / bpm

}
class Stage : Scene() {
    override suspend fun SContainer.sceneMain() {
        val level = Json.decodeFromString<Level>(resourcesVfs["level.json"].readString())
        State(level, 50.degrees, myEasing, Container().addTo(containerRoot)).apply {
            container.note("9BA4B5".hex()) {
                zIndex = 1f
                onEvent(UpdateEvent) { rotation = note.stickAngle.setTo(it.deltaTime) }
            }
            verdict()
        }
    }
}

fun View.configurePosition() {
    if (this is Anchorable) anchor(0.5f, 1 - (width/2) / height)
    centerXOnStage()
    alignY(root, 0.65, true)
}
fun Container.note(color: RGBA, callback: Stick.() -> Unit): Stick {
    val width = 15
    return fastRoundRect(
        corners = RectCorners(1),
        size = Size(width, 500), color = color
    ) {
        configurePosition()
        callback(this)
    }
}

fun State.verdict() {
    container.keys {
        justDown(Key.SPACE) {
            note.alives.fastForEach {
                val sub = note.stickAngle.elapsed - it.note
                val distance = abs(sub.seconds)
                when (distance) {
                    in 0.95..1.05 -> {
                        spawnAudit("Perfect!", it.stick, Audit.PERF)
                    }
                    in 0.75..1.0 -> {
                        spawnAudit(distance.toString(), it.stick, Audit.FAST)
                    }
                    in 1.0..1.25 -> {
                        spawnAudit(distance.toString(), it.stick, Audit.SLOW)
                    }
                    in 0.5..1.0 -> {
                        spawnAudit(distance.toString(), it.stick, Audit.TOO_FAST)
                    }
                    in 1.0..1.5 -> {
                        spawnAudit(distance.toString(), it.stick, Audit.TOO_SLOW)
                    }
                }
            }
        }
    }
}

enum class Audit(val text: String, val color: RGBA) {
    TOO_SLOW("Too slow!", Colors.RED),
    SLOW("Slow!", Colors.YELLOW),
    PERF("Perfect!", Colors.GREEN),
    FAST("Fast!", Colors.YELLOW),
    TOO_FAST("Too fast!", Colors.RED),
}
fun State.spawnAudit(btw: String, view: View, audit: Audit) {
    val angle = view.rotation
    val offset = (-90).degrees
    view.removeFromParent()
    note.alives.fastIterateRemove { it.stick == view }
    container.text(audit.text, 40f, color = audit.color) {
        alignment = TextAlignment.CENTER
        pos = view.pos
        pos = Point(pos.x + 500 * cos(angle + offset), pos.y + 500 * sin(angle + offset))
        var elapsed = 0.milliseconds
        onEvent(UpdateEvent) {
            elapsed += it.deltaTime
            zIndex = 2f
            if (elapsed.seconds >= 1 + bpmToSec) {
                removeFromParent()
            }
        }
    }
}
