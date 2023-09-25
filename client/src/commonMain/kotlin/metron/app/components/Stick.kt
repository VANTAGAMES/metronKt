package metron.app.components

import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import metron.*
import util.*
import kotlin.math.*

val stickArea get() = 0.65f
val stickHeight get() = screen.height*stickArea
val stickWidth get() = stickArea*30f
val stickSize get() = Size(stickWidth, stickHeight)
val stickY get() = (screen.height*2+stickHeight)/3f
fun configureAnchor(container: View) = container.transform {
    if (this is Anchorable) anchor(0.5f, 1 - (width/2) / height)

}
fun configurePositionAndAnchor(container: View) = container.transform {
    configureAnchor(this)
    configurePosition(this)
}

fun configurePosition(container: View) = container.transform {
    centerXOn(screen)
    positionY(stickY)
}
