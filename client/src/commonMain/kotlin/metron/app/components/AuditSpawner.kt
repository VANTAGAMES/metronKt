package metron.app.components

import com.github.quillraven.fleks.*
import metron.util.*

class AuditSpawner : Component<AuditSpawner> {
    override fun type() = AuditSpawner
    companion object : ComponentHooks<AuditSpawner>()
}
