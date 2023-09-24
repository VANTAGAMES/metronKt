package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import metron.*
import metron.util.*

class GhostStickSpawner : Component<GhostStickSpawner> {
    override fun type() = Companion
    companion object : ComponentHooks<GhostStickSpawner>()
}
