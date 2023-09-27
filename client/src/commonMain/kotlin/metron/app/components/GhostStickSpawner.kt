package metron.app.components

import com.github.quillraven.fleks.*
import metron.util.*

class GhostStickSpawner : Component<GhostStickSpawner> {
    override fun type() = Companion
    companion object : ComponentHooks<GhostStickSpawner>()
}
