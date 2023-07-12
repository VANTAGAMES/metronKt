package effect

import State
import korlibs.korge.view.*
import korlibs.time.*
import startStickMove

fun State.magnanimityEffect(period: TimeSpan = (delay).seconds/2, callback: () -> Unit) {
    val startTime = DateTime.now()
    magnanimity = .0
    container.dummyView().apply {
        onEvent(UpdateEvent) {
            val now = DateTime.now()
            val span = now - startTime
            if (span > period) {
                removeFromParent()
                magnanimity = level.magnanimity
                callback()
            } else {
                magnanimity = (span.seconds / period.seconds) * level.magnanimity
            }
        }
    }
    startStickMove()
}
