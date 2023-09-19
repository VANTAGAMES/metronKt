package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import metron.*
import metron.util.*

class GhostSpawner : Component<GhostSpawner> {
    lateinit var ghostContainer: Container
    override fun type() = Companion
    companion object : ComponentHooks<GhostSpawner>() {
        override val onAdded: ComponentHook<GhostSpawner> = { entity, ghostSpawner ->
            ghostSpawner.ghostContainer = screen.container() {
                filter = null
            }
        }
    }
}
