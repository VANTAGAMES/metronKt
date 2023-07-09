import korlibs.event.*
import korlibs.image.text.*
import korlibs.io.lang.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.interpolation.*
import korlibs.math.interpolation.max
import korlibs.math.interpolation.min
import korlibs.time.*
import util.ColorUtil.hex
import kotlin.math.*

fun State.welcomeText() = txtWithFilter(" click space key to start ") {
        keys {
            var cancellable: Cancellable? = null
            cancellable = justDown(Key.SPACE) {
                cancellable?.cancel()
                startStickMove()
                makeThisDisappear()
                ghostSpawner()
//                magnanimityEffect {
                    verdict()
//                }
            }
        }
    }

fun State.txtWithFilter(txt: String, code: Container.() -> Unit) = Container().addTo(container) {
    filter = IdentityFilter
    text(txt, color = ColorPalette.text.hex(), textSize = 40f) {
        alignment = TextAlignment.CENTER
        centerXOnStage()
        alignY(root, 0.1, true)
    }
    code.invoke(this)
}

fun View.makeThisDisappear() {
    var i = 1f
    val originY = pos.y
    zIndex = -1f
    onEvent(UpdateEvent) {
        i -= 0.05f
        if (i <= 0) {
            removeFromParent()
        } else {
            alpha = min(1f, max(0f, Easing.EASE_SINE.invoke(i)))
            positionY(originY + (1 - alpha) * 100)
        }
    }

}
