import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

typealias Stick = FastRoundRect
const val stickHeight = 480

fun View.configurePosition() {
    if (this is Anchorable) anchor(0.5f, 1 - (width/2) / height)
    centerXOnStage()
    alignY(root, 0.8, true)
}
fun Container.note(color: RGBA, callback: Stick.() -> Unit): Stick {
    val width = 12
    return fastRoundRect(
        corners = RectCorners(1),
        size = Size(width, stickHeight), color = color
    ) {
        configurePosition()
        callback(this)
    }
}
