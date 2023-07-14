import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import util.ColorUtil.hex

typealias Stick = FastRoundRect
const val stickHeight = 480

fun State.configurePosition(container: View) {
    container.apply {
        if (this is Anchorable) anchor(0.5f, 1 - (width/2) / height)
    }
    container.centerXOnStage()
    container.positionY(sceneContainer.height*7/8)
}
fun State.note(container: Container, color: RGBA, callback: Stick.() -> Unit): Stick {
    val width = 12
    return container.fastRoundRect(
        corners = RectCorners(1),
        size = Size(width, stickHeight), color = color
    ) {
        configurePosition(this)
        callback(this)
    }
}

fun State.livingStick() {
    container.apply {
        note(this, ColorPalette.stick.hex()) {
            zIndex = 1f
            onEvent(UpdateEvent) {
                if (isPaused) return@onEvent
                rotation = note.stickAngle.setTo(it.deltaTime)
            }
        }
    }
}
