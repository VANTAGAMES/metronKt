package metron.app

import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.time.*
import metron.*
import metron.app.components.*
import metron.app.components.Effect.Companion.easingEffect
import metron.app.components.Effect.Companion.posX
import metron.app.components.Text
import metron.app.entities.*
import metron.app.systems.*
import metron.util.*

suspend fun mainView() {
    val stage = Stage("level1") { stage ->
        components {
            add(GhostStick)
            add(GhostStickSpawner)
            add(PlayerStick)
            add(Audit)
            add(AuditSpawner)
            add(Text)
        }
        systems {
            add(GhostSpawnerSystem(stage))
            add(PlayerStickSystem(stage))
            add(AuditSpawnerSystem(stage))
        }
    }
    stage.createText("스페이스바를 클릭하세요")
    stage.createGhostSpawner()
    stage.createPlayerStick()
    stage.enableInput()
}
