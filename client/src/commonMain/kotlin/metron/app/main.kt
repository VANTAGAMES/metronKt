package metron.app

import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.time.*
import metron.*
import metron.app.components.*
import metron.app.entities.*
import metron.app.systems.*
import metron.util.*

suspend fun mainView() {
    val stage = Stage("level1") { stage ->
        components {
            add(GhostStick)
            add(GhostSpawner)
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
