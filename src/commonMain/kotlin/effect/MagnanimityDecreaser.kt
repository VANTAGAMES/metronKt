package effect

import State
import korlibs.korge.view.*
import korlibs.time.*

fun State.magnanimityDecreasing(period: TimeSpan = (delay).seconds/2, callback: () -> Unit): DummyView {
    val startTime = DateTime.now()
    return container.dummyView().apply {
        onEvent(UpdateEvent) {
            val now = DateTime.now()
            val span = now - startTime
            if (span > period) {
                removeFromParent()
                magnanimity = 0.0
                callback()
            } else {
                val ratio = 1 - (span.seconds / period.seconds)
                magnanimity = ratio * level.magnanimity
            }
        }
    }
}
