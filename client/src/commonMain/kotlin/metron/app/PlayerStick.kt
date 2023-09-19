package metron.app

import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import metron.*
import metron.app.components.*
import metron.app.components.GhostStick.Companion.configurePosition

fun Stage.enablePlayerStick() {
    screen.apply {
        val width = 12
        fastRoundRect(
            corners = RectCorners(1),
            size = Size(width, stickHeight), color = Colors.WHITESMOKE
        ) {
            configurePosition(this)
            zIndex = 1f
            onEvent(UpdateEvent) {
                if (isPaused) return@onEvent
                rotation = setTo(it.deltaTime)
            }
        }
    }
}
