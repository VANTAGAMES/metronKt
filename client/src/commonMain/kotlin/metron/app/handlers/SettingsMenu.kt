package metron.app.handlers

import event.*
import korlibs.image.color.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectPosX
import util.*

fun Stage.enableSettingsMenu() {
    val panel = screen.uiContainer(Size()) {
        transform { size(Size(screen.width, screen.height)).position(screen.width, 0f) }
        val panel = this
        solidRect(size, color = Colors["E3E0D7"]).transform { size(panel.size) }
    }.zIndex(50)
    var isOpened = false
    screen.onEvent(SettingsMenuToggleEvent) {
        if (isOpened == isSettingsMenuOpened) return@onEvent
        isOpened = isSettingsMenuOpened
        if (isSettingsMenuOpened) {
            panel.easingEffect(0.628.seconds, Easing.EASE_OUT_BOUNCE, arrayOf(
                (-panel.width + panel.width - panel.pos.x).run(::effectPosX)
            ))
        } else {
            panel.easingEffect(0.628.seconds, Easing.EASE_OUT_BOUNCE, arrayOf(
                (panel.width-(panel.pos.x)).run(::effectPosX)
            ))
        }
    }
}
