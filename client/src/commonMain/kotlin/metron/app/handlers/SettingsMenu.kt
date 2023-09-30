package metron.app.handlers

import event.*
import korlibs.event.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.input.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.memory.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import metron.event.*
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectPosX
import util.*
import kotlin.math.*

fun Stage.enableSettingsMenu() {
    val panel = screen.uiContainer(Size()) {
        styles {
            textFont = mediumFont
            textColor = globalTextColor
            textAlignment = TextAlignment.MIDDLE_CENTER
        }
        position(screen.width, 0f)
        transform { size(Size(screen.width, screen.height)) }
        val panel = this
        val background = solidRect(size, color = Colors["808080"])
            .transform { size(panel.size) }
        uiVerticalStack(width = width, adjustSize = false) {
            uiSpacing(Size(0f, 144f))
            uiText("설정") {
                styles.textSize = 100f
            }.transform { centerXOn(panel) }
            uiSpacing(Size(0f, 250f))
            val sliderWidth = 100f
            uiContainer(Size()) slider@{
                transform { size(Size(screen.width/1.5f, sliderWidth)) }
                fun formattedOffset() = "<  오프셋: $preferenceOffset  >"
                val text = uiText(formattedOffset()) {
                    styles.textSize = 70f
                    styles.textAlignment = TextAlignment.MIDDLE_CENTER
                }.transform { centerXOn(this@slider).positionY(-70) }
                val borderSize = 10f
                val selectorBorder = Size(borderSize, borderSize)
                val selector = uiContainer(size + selectorBorder * 2)
                    .transform { centerOn(this@slider).position(-selectorBorder.toPoint()) }
                val border = fastRoundRect(size, color = styles.textColor, corners = RectCorners(.5))
                    .transform { size(this@slider.size) }
                fastRoundRect(Size(), color = background.color, corners = RectCorners(.5))
                    .transform { size(this@slider.size-selectorBorder*2) }
                    .centerOn(border)
                val maxSlide = 300f
                val step = 5f
                val button = roundRect(
                    Size(sliderWidth * 1.5f, sliderWidth),
                    radius = RectCorners((sliderWidth - (sliderWidth - borderSize))),
                    fill = styles.textColor
                ).transform {
                    positionX(preferenceOffset/maxSlide*(this@slider.width - width))
                }
                button.keys {
                    down(Key.LEFT, Key.RIGHT) {
                        if (!isSettingsMenuOpened) return@down
                        preferenceOffset += when (it.key) {
                            Key.LEFT -> -step.toInt()
                            Key.RIGHT -> step.toInt()
                            else -> 0
                        }
                        button.dispatch(ResizedEvent())
                        text.setText(formattedOffset())
                    }
                }
                button.draggable(selector) {
                    val length = width - button.width
                    val nextX = globalToLocal(Point(it.sx + it.dx, 0f)).x
                        .minus(button.width / 2)
                        .let { max(0f, min(length, it)) }
                        .let { ((((it / length) * maxSlide) / step).toIntCeil() * step).toIntFloor() }
                    preferenceOffset = nextX
                    text.setText(formattedOffset())
                    button.position(nextX / maxSlide * length, border.y)
                }
            }.transform { centerXOn(panel) }
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
                (panel.width+(panel.pos.x)).run(::effectPosX)
            )) { positionX(width) }
        }
    }
}
