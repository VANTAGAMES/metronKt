package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import metron.*
import metron.app.Stage
import metron.util.*
import util.*
import kotlin.math.*

data class Text(val stage: Stage, val text: String) : Component<Text> {
    lateinit var body: View
    override fun type() = Companion
    companion object : ComponentHooks<Text>() {
        override val onAdded: ComponentHook<Text> = { entity, text ->
            text.enableBody()
        }
    }
    fun enableBody() {
        val textHeight = screen.height
        val stage = stage
        body = screen.uiText(
            text,
            size = Size(screen.width, textHeight)
        ) {
            styles.textFont = stage.boldFont
            styles.textAlignment = TextAlignment.MIDDLE_CENTER
            styles.textColor = Colors["DDE6ED"]
            transform {
                size(Size(screen.width, textHeight))
                styles.textSize = 40 * ((screen.height / scene.height).pow(1f))
                println(styles.textSize)
                filter = BlurFilter((sqrt(screen.height / scene.height).pow(1f)-1).apply(::println))
                centerXOn(screen)
                positionY((screen.height - stickHeight)/4 - textHeight/2f + stickHeight/20)
            }
        }
    }
}
