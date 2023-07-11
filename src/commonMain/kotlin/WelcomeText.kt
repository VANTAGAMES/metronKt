import event.*
import korlibs.audio.sound.*
import korlibs.event.*
import korlibs.image.text.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.math.interpolation.max
import korlibs.math.interpolation.min
import korlibs.time.*
import kotlinx.coroutines.*
import util.ColorUtil.hex
import kotlin.math.*

fun State.welcomeText() = txtWithFilter(" click space key to start ") {
        keys {
            var cancellable: Cancellable? = null
            cancellable = onEvent(HitEvent) {
                cancellable?.cancel()
                makeThisDisappear()
                countdownText()
                ghostSpawner()
                magnanimityEffect {
                    combo()
                    verdict()
                }
            }
        }
    }

fun State.txtWithFilter(txt: String, code: Container.() -> Unit) = Container().addTo(container) {
    filter = IdentityFilter
    textBlock(
        RichTextData(txt, color = ColorPalette.text.hex(), textSize = 40f),
        size = Size(1000, 95),
        align = TextAlignment.MIDDLE_CENTER
    ) {
        centerXOnStage()
        alignY(root, 0.05, true)
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
