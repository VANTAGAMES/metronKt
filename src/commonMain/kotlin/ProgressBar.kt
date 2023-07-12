import effect.*
import event.*
import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.ColorUtil.hex
import kotlin.math.*

fun State.progressBar() {
    val thick = 5f
    container.solidRect(sceneContainer.width, thick, color = ColorPalette.text.hex()) {
        pos = Point(.0f, sceneContainer.height - thick)
        lateinit var cancellable1: Cancellable
        cancellable1 = onEvent(UpdateEvent) {
            val ratio = max(0f, (min(1f, note.stickAngle.elapsed / level.playingTime)))
            scaledWidth = ratio * sceneContainer.width
            if (ratio >= 1) {
                cancellable1.cancel()
                container.dispatch(GameEndEvent())
            }
        }
        lateinit var cancellable2: Cancellable
        cancellable2 = onEvent(GameEndEvent) {
            cancellable2.cancel()
            val blur = blur(from = 0f, to = 20f, easing = Easing.EASE_OUT_BOUNCE, period = bpmToSec.seconds * 4) {}
            magnanimityDecreasing(bpmToSec.seconds * 2) {

            lateinit var cancellable3: Cancellable
            cancellable3 = onEvent(HitEvent) {
                cancellable3.cancel()
                launchImmediately(currentCoroutineContext) {
                    note = note(view = note.view)
                    countdownText()
                    println(magnanimity)
                    magnanimityEffect(magnanimity) {}
                    blur.removeFromParent()
                    progressBar()
                    val from = (container.filter as? BlurFilter)?.radius ?: 0f
                    blur(from = from, to = 0f, easing = Easing.EASE_OUT, period = bpmToSec.seconds / 2)
                }
            }
        }
        }

    }

}
