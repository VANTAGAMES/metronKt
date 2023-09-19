package metron.app

import metron.app.components.*
import metron.app.entities.*
import metron.app.systems.*
import metron.util.*

suspend fun mainView() {
    val stage = Stage("level1") { stage ->
        components {
            add(GhostStick)
            add(GhostStickSpawner)
            add(PlayerStick)
        }
        systems {
            add(GhostSpawnerSystem(stage))
            add(PlayerStickSystem(stage))
            add(AuditSpawnerSystem(stage))
        }
    }
    stage.createGhostSpawner()
    stage.createPlayerStick()
    stage.enableInput()
}
