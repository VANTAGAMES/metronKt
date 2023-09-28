package metron.util

import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.time.*

fun View.addTimer(timeSpan: TimeSpan? = null, callback: (TimeSpan) -> Unit): Cancellable {
    var cancellable: Cancellable? = null
    var elapsed = 0.seconds
    cancellable = addUpdater(first = false) {
        elapsed += it
        if (elapsed == 0.seconds) return@addUpdater
        if (timeSpan == null || elapsed >= timeSpan) {
            cancellable?.cancel()
            callback(it)
        }
    }
    return cancellable
}
