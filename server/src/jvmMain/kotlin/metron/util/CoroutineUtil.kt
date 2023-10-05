package metron.util

import korlibs.io.async.*
import kotlinx.coroutines.*
import kotlin.coroutines.*


fun launchNow(context: CoroutineContext = GlobalScope.coroutineContext, callback: suspend () -> Unit) {
    CoroutineScope(context).launchImmediately(callback)
}
