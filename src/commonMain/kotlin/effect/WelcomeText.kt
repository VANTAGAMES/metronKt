package effect

import ColorPalette
import State
import combo
import event.*
import ghostSpawner
import korlibs.image.text.*
import korlibs.io.lang.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import progressbar
import util.ColorUtil.hex
import verdict
import kotlin.math.*

fun State.welcomeText() = txtWithFilter(" 스페이스바를 클릭하세요 ") {
    visible = false
    val showCancel = showUpThis(this, easing = Easing.EASE_IN) {  }
        keys {
            var cancellable: Cancellable? = null
            cancellable = onEvent(HitEvent) {
                cancellable?.cancel()
                initGame()
                showCancel.cancel()
                hideIt(this@txtWithFilter, period = bpmToSec.seconds/3) {  }
            }
        }
    }

fun State.initGame() {
    isPaused = false
    progressbar()
    countdownText()
    ghostSpawner()
    combo()
    verdict()
    lateinit var cancellable: Cancellable
    cancellable = container.onEvent(UpdateEvent) {
        cancellable.cancel()
        playingMusic = music.playNoCancel()
        playingMusic.volume = 0.5
    }
    //                container.addUpdater((bpmToSec).timesPerSecond) {
//                    audit(0.seconds, null)
//                }
    magnanimityEffect {
        magnanimity = level.magnanimity
    }
    note.stickAngle.resetElapsed()
}

fun State.txtWithFilter(txt: String, code: Container.() -> Unit) = Container().addTo(container) {
    filter = IdentityFilter
    textBlock(
        RichTextData(txt, color = ColorPalette.text.hex(), textSize = 40f, font = boldFont),
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
