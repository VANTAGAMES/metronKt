package metron.app.components

import event.*
import korlibs.datastructure.iterators.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.util.*
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectAlpha
import util.*

fun Stage.enableGameFinishing() {
    screen.onEvent(GameEndEvent) {
        if (isStopped) return@onEvent
        isStopped = true
        println("A")
        screen.dummyView().easingEffect((delay / 2).seconds, Easing.EASE, arrayOf(
            Effect { _, value -> magnanimity = 1 - value * level.magnanimity }
        )) { magnanimity = .0; removeFromParent() }
        noteIterator = mutableListOf<Double>().iterator()
        lives.fastForEach {
            it.body.easingEffect(bpmToSec.seconds/6, Easing.EASE, arrayOf(
                effectAlpha(1f)
            )) { removeFromParent() }
        }
        lives.clear()
        if (it.isSuccess) {
            screen.uiText("클리어!") {
                styles(defaultStyle)
                centerOn(screen)
            }
            screen.dummyView().easingEffect(1.seconds, Easing.EASE, arrayOf(
                Effect { _, value -> screen.filter = BlurFilter(value * 4f) }
            )) { removeFromParent() }
        } else {
            launchNow {
                val completionPercentage = (elapsedSeconds / playingTime * 100).toInt()
                enableIntro("$completionPercentage% 완료")
            }
        }
    }
}
