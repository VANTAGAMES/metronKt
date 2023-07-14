package effect

import ColorPalette
import State
import autoMacro
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
    score()
    combo()
    verdict()
    autoMacro()
    lateinit var cancellable: Cancellable
    var elapsed = 0.seconds
    cancellable = container.onEvent(UpdateEvent) {
        elapsed += it.deltaTime
        val offset = max(.0, offset*-1).seconds
        if (elapsed < offset) return@onEvent
        cancellable.cancel()
        playingMusic = music.playNoCancel()
    }
    //                container.addUpdater((bpmToSec).timesPerSecond) {
//                    audit(0.seconds, null)
//                }
    magnanimityEffect {
        magnanimity = level.magnanimity
    }
    note.stickAngle.resetElapsed()
}

fun State.txtWithFilter(txt: String, parent: Container = screenContainer, code: Container.() -> Unit) = Container().addTo(parent) {
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
