package metron.app

import metron.app.components.*
import metron.app.entities.*
import metron.app.systems.*
import metron.util.*

suspend fun mainView() = Stage("level1") { stage ->
    components {
        add(GhostStick)
        add(GhostStickSpawner)
        add(PlayerStick)
        add(Audit)
        add(AuditSpawner)
    }
    systems {
        add(GhostSpawnerSystem(stage))
        add(PlayerStickSystem(stage))
        add(AuditSpawnerSystem(stage))
    }
}.apply {
    createIntro()
    createGhostSpawner()
    createPlayerStick()
    enableInput()
}
