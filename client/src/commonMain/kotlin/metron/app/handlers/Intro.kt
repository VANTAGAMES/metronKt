package metron.app.handlers

import event.*
import korlibs.io.lang.*
import korlibs.korge.time.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.util.*
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.effectPosY
import util.*
import kotlin.math.*

fun Stage.enableIntro(intro: String = "클릭해서 시작하세요") {
    val title = createTitle(intro) {
        transform { centerXOn(screen) }
        easingEffect(
            0.7.seconds, Easing.EASE_OUT, arrayOf(
                effectAlpha(1.25f),
                effectPosY(70f)
            )
        )
    }
    val hitEventNode = screen.dummyView()
    hitEventNode.onEvent(HitEvent) {
        if (isStopped && elapsedSeconds != 0.seconds) {
            println("Af")
            return@onEvent
        }
        countdown()
        hitEventNode.removeFromParent()
        title.easingEffect((bpmToSec/3).seconds, Easing.EASE_OUT, arrayOf(
            effectAlpha(1f, isDown = true),
            effectPosY(70f)
        )) { removeFromParent() }
    }
}

fun Stage.countdown(times: Int = 4, callback: (TimeSpan) -> Unit = {}) {
    if (elapsedSeconds != 0.seconds) {
        isForcePaused = true
        isPausedByUser = false
        isStopped = true
        screen.dummyView().apply {
            addTimer(-defaultElapsed()) {
                isForcePaused = false
                isPausedByUser = false
                isStopped = false
                removeFromParent()
                callback(it)
            }.also { cancellable ->
                onEvent(SettingsMenuToggleEvent) {
                    cancellable.cancel()
                }
            }
        }
    } else {
        elapsedSeconds = defaultElapsed()
        noteIterator = level.map.iterator()
        previousNote = .0
        currentNote = .0
        screen.dispatch(GameStartEvent())
        isForcePaused = false
        isPausedByUser = false
        isStopped = false
        screen.dummyView().apply {
            onEvent(SettingsMenuToggleEvent) {
                removeFromParent()
                elapsedSeconds = 0.seconds
                magnanimity = .0
            }
            easingEffect((delay/2).seconds, Easing.EASE, arrayOf(
                Effect { _, value -> magnanimity = value * targetMagnanimity }
            )) { magnanimity = targetMagnanimity; removeFromParent() }
        }
    }
    (1..times).forEach { num ->
        val countdownEffectPeriod = (bpmToSec / 3).seconds
        screen.dummyView().apply {
            timeout((num * bpmToSec - initialNote * bpmToSec + max(.0, offsetToSec)).seconds) {
                if (isPausedByUser) return@timeout
                launchNow { hitSound.play() }
                val isStart = num == times
                createTitle(if (isStart) "시작!" else " ${times - num} ", fontSize = 45) {
                    transform { centerXOn(screen) }
                    easingEffect(
                        countdownEffectPeriod, Easing.EASE_IN_QUAD, arrayOf(
                            effectAlpha(1f),
                            effectPosY(100f * countdownEffectPeriod.seconds.toFloat())
                        )
                    ) {
                        easingEffect(
                            countdownEffectPeriod, Easing.EASE_OUT_QUAD, arrayOf(
                                effectAlpha(1f, isDown = true),
                                effectPosY(100f * countdownEffectPeriod.seconds.toFloat())
                            )
                        ) {
                            removeFromParent()
                        }
                    }
                }
            }.also { closeable ->
                onEvent(SettingsMenuToggleEvent) {
                    closeable.close()
                }
            }
        }
    }
}
