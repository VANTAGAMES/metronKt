package metron.app.systems

import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.World.Companion.family
import event.*
import korlibs.datastructure.iterators.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.app.components.*
import metron.app.entities.*
import util.*

class AuditSpawnerSystem(private val stage: Stage) : IteratingSystem(
    family { all(AuditSpawner) },
    interval = EachFrame
) {
    override fun onTickEntity(entity: Entity) {
        if (stage.elapsedSeconds < 0.seconds) return
        stage.audit(deltaTime.seconds)
    }
    companion object {
        private val auditTypes = AuditType.values()
        fun Stage.audit(delta: TimeSpan) {
            alives.fastForEach { ghost ->
                val sub = elapsedSeconds+delta - ghost.nextNote
                run {
                    auditTypes.fastForEach { audit ->
                        if (sub.seconds in audit.range) {
                            launchNow { hitSound.play() }
                            createAudit(ghost, audit)
                            screen.dispatch(AuditEvent(ghost, audit))
                            return@run
                        }
                    }
                }
            }
        }

    }
}

