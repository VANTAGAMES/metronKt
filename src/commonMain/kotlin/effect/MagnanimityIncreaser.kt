package effect

import State
import korlibs.korge.view.*
import korlibs.time.*

fun State.magnanimityEffect(origin: Double = .0, period: TimeSpan = (delay).seconds/2, callback: () -> Unit) {
    magnanimity = origin
    val startTime = DateTime.now()
    container.dummyView().apply {
        onEvent(UpdateEvent) {
            val now = DateTime.now()
            val span = now - startTime
            if (span > period) {
                removeFromParent()
                callback()
            } else {
                magnanimity = (span.seconds / period.seconds) * level.magnanimity
            }
        }
    }
}
