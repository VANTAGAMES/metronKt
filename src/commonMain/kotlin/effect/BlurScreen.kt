package effect

import State
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlin.math.*

fun State.blur(
    from: Float = 20f, to: Float = 0f,
    period: TimeSpan = 1.seconds, easing: Easing, callback: () -> Unit = {}
): DummyView {
    val blur = BlurFilter(from)
    container.filter = blur
    val startTime = DateTime.now()
    return container.dummyView().apply {
        onEvent(UpdateEvent) {
            val now = DateTime.now()
            val span = now - startTime
            if (span > period) {
                removeFromParent()
                callback()
            } else {
                var ratio = easing((span.seconds / period.seconds).toFloat())
                if (from > to) ratio = 1 - ratio
                blur.radius = ratio.toRatio().interpolate(min(from, to), max(from, to))
            }
        }
    }
}
