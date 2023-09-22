package metron.app.components

import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import metron.*
import util.*
import kotlin.math.*

val stickHeight get() = screen.height*2/3
val stickWidth get() = sqrt(stickHeight).pow(0.9f)
val stickSize get() = Size(stickWidth, stickHeight)
val stickY get() = screen.height*8/9
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
