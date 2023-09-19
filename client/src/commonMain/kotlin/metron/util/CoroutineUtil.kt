package util

import korlibs.io.async.launchImmediately
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlin.coroutines.CoroutineContext

fun launchNow(context: CoroutineContext = GlobalScope.coroutineContext, callback: suspend () -> Unit) {
    CoroutineScope(context).launchImmediately(callback)
}
