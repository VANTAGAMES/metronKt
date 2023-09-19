package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import metron.*
import metron.util.*

class GhostStickSpawner : Component<GhostStickSpawner> {
    lateinit var ghostContainer: Container
    override fun type() = Companion
    companion object : ComponentHooks<GhostStickSpawner>() {
        override val onAdded: ComponentHook<GhostStickSpawner> = { entity, ghostSpawner ->
            ghostSpawner.ghostContainer = screen.container() {
                filter = null
            }
        }
    }
}
