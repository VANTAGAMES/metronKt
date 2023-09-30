package metron.app.handlers

import event.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
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
        styles {
            textFont = mediumFont
            textColor = globalTextColor
            textAlignment = TextAlignment.MIDDLE_CENTER
        }
        transform { size(Size(screen.width, screen.height)).position(screen.width, 0f) }
        val panel = this
        solidRect(size, color = Colors["808080"]).transform { size(panel.size) }
        uiVerticalStack(width = width, adjustSize = false) {
            uiSpacing(Size(width, 144f))
            uiText("설정") {
                styles.textSize = 100f
            }.centerXOn(panel)
            uiSpacing(Size(width, 200f))
            uiContainer {
                uiText("내용이 없습니다") {
                    styles.textSize = 70f
                    styles.textAlignment = TextAlignment.MIDDLE_CENTER
                }.centerXOn(panel)
            }
        }
    }.zIndex(50)
    var isOpened = false
    screen.onEvent(SettingsMenuToggleEvent) {
        if (isOpened == isSettingsMenuOpened) return@onEvent
        isOpened = isSettingsMenuOpened
        if (isSettingsMenuOpened) {
            panel.easingEffect(0.628.seconds, Easing.EASE_OUT_BOUNCE, arrayOf(
                (-panel.width + panel.width - panel.pos.x).run(::effectPosX)
            )) { positionX(0f) }
        } else {
            panel.easingEffect(0.628.seconds, Easing.EASE_OUT_BOUNCE, arrayOf(
                (panel.width-(panel.pos.x)).run(::effectPosX)
            )) { positionX(width) }
        }
    }
}
