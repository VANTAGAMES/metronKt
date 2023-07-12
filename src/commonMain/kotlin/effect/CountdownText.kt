package effect

import State
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*

fun State.countdownText() {
    val now = DateTime.now()
    val count = 4
    (1..count).forEach { num ->
        txtWithFilter(if (num == count) "시작!" else " ${count - num} ") {
            alpha = 1f
            visible = false
            showUpThis(
                startCode = {
                    hitSound.playNoCancel()
                },
                startTime = (now - bpmToSec.seconds/2) + ((num).seconds) * bpmToSec,
                period = bpmToSec.seconds/3
            ) {
                hideIt(period = bpmToSec.seconds/3) {
                    removeFromParent()
                }
            }
        }
    }

}

fun View.hideIt(period: TimeSpan = 0.7.seconds, easing: Easing = Easing.EASE_OUT, ay: Double = 100.0 * period.seconds, callback: () -> Unit): Cancellable {
    val startTime = DateTime.now()
    val originY = pos.y
    zIndex = 10f
    lateinit var listener: Cancellable
    listener = onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            visible = false
            listener.cancel()
            callback()
        } else {
            val i = (span / period)
            alpha = 1 - kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
            positionY(originY + (1 - alpha) * ay)
        }
    }
    return listener

}

fun View.showUpThis(
    startCode: () -> Unit = {},
    startTime: DateTime = DateTime.now(), period: TimeSpan = 0.7.seconds,
    easing: Easing = Easing.EASE_IN, isUp: Boolean = false, callback: () -> Unit,
): CloseableCancellable {
    val originY = pos.y
    zIndex = 10f
    var listener: CloseableCancellable? = null
    var isStarted = false
    val code = Runnable {
        val now = DateTime.now()
        if (startTime > now) return@Runnable
        if (!isStarted) {
            isStarted = true
            startCode.invoke()
        }
        val span = now - startTime
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
    listener = onEvent(UpdateEvent) { code.run() }
    code.run()
    return listener
}
