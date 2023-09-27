package util

import korlibs.korge.view.*
import metron.event.*

fun <T : View> T.transform(code: T.() -> Unit): T {
    code(this)
    onEvent(ResizedEvent) { code(this) }
    return this
}
