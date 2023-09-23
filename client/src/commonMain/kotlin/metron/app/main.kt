package metron.app

import korlibs.korge.ui.*
import metron.*
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
    enableMusic()
    enableIntro()
    createGhostSpawner()
    createPlayerStick()
    enableInput()
    enableCombo()
    enableProgressbar()
    enableScore()
    enableSongTitle()
    enableDeath()
    enableGameFinishing()
}
