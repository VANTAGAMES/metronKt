import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*

fun State.countdownText() {
    val now = DateTime.now()
    val count = 3
    (1..count).forEach { num ->
        txtWithFilter(" ${count - num+1} ") {
            alpha = 1f
            visible = false
            showUpThis(startCode = {
                hitSound.playNoCancel()
            }, startTime = (now-bpmToSec.seconds/2) + num.seconds*bpmToSec) {
                hideIt {
                    removeFromParent()
                }
            }
        }
    }

}

fun View.hideIt(period: TimeSpan = 0.7.seconds, easing: Easing = Easing.EASE_OUT, callback: () -> Unit) {
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
            positionY(originY + (1 - alpha) * 100)
        }
    }

}

fun View.showUpThis(
    startCode: () -> Unit = {},
    startTime: DateTime = DateTime.now(), period: TimeSpan = 0.7.seconds,
    easing: Easing = Easing.EASE_IN, isUp: Boolean = false, callback: () -> Unit,
) {
    val originY = pos.y
    zIndex = 10f
    var listener: CloseableCancellable? = null
    var isStarted = false
    listener = onEvent(UpdateEvent) {
        val now = DateTime.now()
        if (startTime > now) return@onEvent
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
            positionY(originY - (1 - alpha) * 100)
        }
        if (!visible) visible = true
    }

}
