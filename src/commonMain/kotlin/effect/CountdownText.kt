package effect

import State
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*

fun State.countdownText() {
    val count = 4
    (1..count).forEach { num ->
        txtWithFilter(if (num == count) "시작!" else " ${count - num} ") {
            alpha = 1f
            visible = false
            showUpThis(
                this,
                startCode = {
                    hitSound.playNoCancel()
                },
                startTime = ((num).seconds) * bpmToSec - (bpmToSec.seconds/2),
                period = bpmToSec.seconds/3
            ) {
                hideIt(this, period = bpmToSec.seconds/3) {
                    removeFromParent()
                }
            }
        }
    }

}

fun State.hideIt(view: View, period: TimeSpan = 0.7.seconds, easing: Easing = Easing.EASE_OUT, ay: Double = 100.0 * period.seconds, callback: () -> Unit): Cancellable {
    view.run {
        val originY = pos.y
        var elapsed = 0.seconds
        zIndex = 10f
        lateinit var listener: Cancellable
        listener = onEvent(UpdateEvent) {
            if (isPaused) return@onEvent
                elapsed += it.deltaTime
            if (elapsed >= period) {
                visible = false
                listener.cancel()
                callback()
            } else {
                val i = (elapsed / period)
                alpha = 1 - kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
                positionY(originY + (1 - alpha) * ay)
            }
        }
        return listener
    }
}

fun State.showUpThis(
    view: View,
    startCode: () -> Unit = {},
    startTime: TimeSpan = 0.seconds, period: TimeSpan = 0.7.seconds,
    easing: Easing = Easing.EASE_IN, isUp: Boolean = false, callback: () -> Unit,
): CloseableCancellable = view.run {
    val originY = pos.y
    zIndex = 10f
    var listener: CloseableCancellable? = null
    var span = 0.seconds
    var isStarted = false
    val code: (TimeSpan) -> Unit =  code@{
        if (isPaused) return@code
        span += it
        if (startTime > span) return@code
        if (!isStarted) {
            isStarted = true
            startCode.invoke()
        }
        if (span >= period) {
            callback()
            listener?.cancel()
        } else {
            var i = (span / period)
            if (!isUp) i = 1 - i
            alpha = 1 - kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
            positionY(originY - (1 - alpha) * 100*period.seconds)
        }
        if (!visible) visible = true
    }
    listener = onEvent(UpdateEvent) {
        if (isPaused) return@onEvent
        code(it.deltaTime)
    }
    code(0.seconds)
    return listener
}
