package metron.app

import metron.app.components.*
import metron.app.entities.*
import metron.app.handlers.*
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
    enableHitEffect()
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
