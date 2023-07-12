import effect.*
import event.*
import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import util.ColorUtil.hex
import kotlin.coroutines.*
import kotlin.math.*

fun State.progressBar() {
    val thick = 5f
    container.solidRect(sceneContainer.width, thick, color = ColorPalette.text.hex()) {
        pos = Point(.0f, sceneContainer.height - thick)
        onEvent(UpdateEvent) {
            val ratio = max(0f, (min(1f, livingStick.stick.elapsed/level.playingTime)))
            scaledWidth = ratio * sceneContainer.width
            if (ratio >= 1) {
                container.dispatch(GameEndEvent())
            }
        }
        lateinit var cancellable: Cancellable
        cancellable = onEvent(GameEndEvent) {
            cancellable.cancel()
            magnanimityDecreasing(bpmToSec.seconds*2) {
                onEvent(HitEvent) {
                    suspend {
                        this@progressBar.reloadStage()
                    }
                }
            }
            blur(from = 0f, to = 50f, easing = Easing.EASE_OUT_BOUNCE, period = bpmToSec.seconds*8) {
            }
        }
    }

}
