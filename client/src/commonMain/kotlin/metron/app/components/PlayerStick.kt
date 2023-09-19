package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import metron.*
import metron.app.Stage
import metron.app.components.GhostStick.Companion.configurePosition
import metron.util.*

class PlayerStick(val stage: Stage) : Component<PlayerStick> {
    lateinit var body: View
    override fun type() = Companion
    companion object : ComponentHooks<PlayerStick>() {
        override val onAdded: ComponentHook<PlayerStick> = { entity, playerStick ->
            playerStick.body = screen.fastRoundRect(
                corners = RectCorners(1),
                size = Size(stickWidth, stickHeight), color = Colors.WHITESMOKE
            ) {
                configurePosition(this)
                zIndex = 1f
            }
        }
    }
}
