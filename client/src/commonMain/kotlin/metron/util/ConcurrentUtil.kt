package util

import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.time.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext

fun launchNow(context: CoroutineContext = GlobalScope.coroutineContext, callback: suspend () -> Unit) {
    CoroutineScope(context).launchImmediately(callback)
}

fun launchAsync(context: CoroutineContext = GlobalScope.coroutineContext, callback: suspend () -> Unit) {
    CoroutineScope(context).asyncImmediately(callback)
}

fun View.schedule(timeSpan: TimeSpan, onDelay: (TimeSpan) -> Unit, onStopped: () -> Unit): Cancellable {
    val startTime = DateTime.now()
    var cancelllable: Cancellable? = null
    cancelllable = addOptFixedUpdater { event ->
        val elapsed = DateTime.now() - startTime
        if (elapsed >= timeSpan) {
            onStopped()
            cancelllable!!.cancel()
        } else {
            onDelay(elapsed)
        }
    }
    return cancelllable
}
