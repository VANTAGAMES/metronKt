package metron.app.components

import event.*
import korlibs.korge.time.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.interpolation.*
import korlibs.memory.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.util.Effect
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.effectPosY
import util.*
import kotlin.math.*

val isMobile get() = Platform.isAndroid || Platform.isIos

fun Stage.enableIntro(intro: String = if (isMobile) "클릭해서 시작하세요" else "스페이스바를 클릭하세요") {
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
        if (elapsedSeconds != 0.seconds) return@onEvent
        hitEventNode.removeFromParent()
        elapsedSeconds = defaultElapsed()
        isForcePaused = false
        isStopped = false
        noteIterator = level.map.iterator()
        previousNote = .0
        currentNote = .0
        screen.dispatch(GameStartEvent())
        title.easingEffect((bpmToSec/3).seconds, Easing.EASE_OUT, arrayOf(
            effectAlpha(1f, isDown = true),
            effectPosY(70f)
        )) { removeFromParent() }
        countdown()
        screen.dummyView().easingEffect((delay/2).seconds, Easing.EASE, arrayOf(
            Effect { _, value -> magnanimity = value * targetMagnanimity }
        )) { magnanimity = targetMagnanimity; removeFromParent() }
    }
}

fun Stage.countdown(times: Int = 4) {
    (1..times).forEach { num ->
        val countdownEffectPeriod = (bpmToSec / 3).seconds
        screen.timeout((num * bpmToSec - initialNote * bpmToSec + max(.0, offsetToSec)).seconds) {
            launchNow { hitSound.play() }
            createTitle(if (num == times) "시작!" else " ${times - num} ") {
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
        }
    }
}
