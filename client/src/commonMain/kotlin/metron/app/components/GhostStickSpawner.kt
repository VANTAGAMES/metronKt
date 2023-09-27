package metron.app.components

import com.github.quillraven.fleks.*
import metron.app.*
import metron.util.*

fun Stage.createGhostSpawner() = world.entity {
    it += GhostStickSpawner()
}

class GhostStickSpawner : Component<GhostStickSpawner> {
    override fun type() = Companion
    companion object : ComponentHooks<GhostStickSpawner>()
}
