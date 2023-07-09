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
            showUpThis(startTime = (now-(1.seconds)) + num.seconds) {
                hideIt {

                }
            }
        }
    }

}

fun View.hideIt(period: TimeSpan = 1.seconds, easing: Easing = Easing.EASE_OUT, callback: () -> Unit) {
    val startTime = DateTime.now()
    val originY = pos.y
    zIndex = 10f
    onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            callback()
            removeFromParent()
        } else {
            val i = (span / period)
            println(i)
            alpha = 1 - kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
            positionY(originY + (1 - alpha) * 100)
        }
    }

}


fun View.showUpThis(startTime: DateTime = DateTime.now(), period: TimeSpan = 1.seconds, easing: Easing = Easing.EASE_IN, callback: () -> Unit) {
    val originY = pos.y
    zIndex = 10f
    var listener: CloseableCancellable? = null
    visible = false
    listener = onEvent(UpdateEvent) {
        val now = DateTime.now()
        if (startTime >= now) return@onEvent
        val span = now - startTime
        if (span >= period) {
            callback()
            listener?.cancel()
        } else {
            val i = 1 - (span / period)
            println(i)
            alpha = 1 - kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
            positionY(originY - (1 - alpha) * 100)
        }
        if (!visible) visible = true
    }

}
