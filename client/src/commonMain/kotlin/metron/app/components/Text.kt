package metron.app.components

import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import metron.*
import metron.app.Stage
import util.*
import kotlin.math.*

fun Stage.createTitle(text: String) = screen.uiText(text, size = screen.size) {
    styles {
        textFont = boldFont
        textAlignment = TextAlignment.MIDDLE_CENTER
        textColor = Colors["DDE6ED"]
    }
    transform {
        size(screen.size).centerXOn(screen)
        styles.textSize = 41 * ((screen.height / scene.height))
        filter = BlurFilter((sqrt(screen.height / scene.height) - 1))
        positionY((screen.height - stickHeight) / 4 - screen.height / 2f)
    }
}
