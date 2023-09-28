package metron.util

import korlibs.io.lang.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*

fun View.addTimer(
    timeSpan: TimeSpan? = null, interval: (TimeSpan) -> TimeSpan = { it }, callback: (TimeSpan) -> Unit
): Cancellable {
    var cancellable: Cancellable? = null
    var elapsed = 0.seconds
    cancellable = addUpdater(first = false) {
        elapsed += interval(it).also { result ->
            if (result == 0.seconds) return@addUpdater
        }
        if (elapsed == 0.seconds) return@addUpdater
        if (timeSpan == null || elapsed >= timeSpan) {
            cancellable?.cancel()
            callback(it)
        }
    }
    return cancellable
}

