package metron.app.handlers

import korlibs.datastructure.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import metron.*
import metron.app.Stage
import metron.util.*
import util.*

val globalTextColor = Colors["DDE6ED"]
val titlePosY get() = screen.height / -2.3f
fun Stage.createTitle(text: String, fontSize: Int = 41, configuration: UIText.() -> Unit = {}): UIText {
    return screen.uiText(text, size = screen.size) {
        styles {
            textFont = boldFont
            textAlignment = TextAlignment.MIDDLE_CENTER
            textColor = globalTextColor
        }
        transform {
            size(screen.size)
            styles.textSize = fontSize * ((screen.height / scene.height))
//        filter = BlurFilter((sqrt(screen.height / scene.height) - 1))
            positionY(getExtra(Effect.EffectedPosY)?.fastCastTo<Float>()?.times(screen.height)
                ?: (titlePosY))
        }
        configuration(this)
    }
}
