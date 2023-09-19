package metron.app.components

import com.github.quillraven.fleks.*
import metron.util.*

class AuditSpawner : Component<AuditSpawner> {
    override fun type() = Companion
    companion object : ComponentHooks<AuditSpawner>()
}
