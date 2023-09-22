package metron.app.components

import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import metron.*
import metron.app.*
import metron.app.Stage
import util.*

fun Stage.enableSongTitle() {
    screen.uiText("${level.author} - ${level.name}") {
        styles {
            textFont = mediumFont
            textColor = globalTextColor
            textSize = 40f
            textAlignment = TextAlignment.MIDDLE_LEFT
        }
        transform {
            val padding = 20
            positionY(height + padding)
            positionX(padding)
        }
    }
}
