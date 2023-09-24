package metron.app.systems

import com.github.quillraven.fleks.*
import com.github.quillraven.fleks.World.Companion.family
import event.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.components.*
import metron.app.entities.*

class GhostSpawnerSystem(private val stage: Stage) : IteratingSystem(
    family {
        all(GhostStickSpawner)
    },
) {
    override fun onTickEntity(entity: Entity): Unit = stage.run {
        if (isPaused || isStopped) return
        if (!noteIterator.hasNext()) return
        val nextSec = currentNote.seconds * bpmToSec
        val prevSec = previousNote.seconds * bpmToSec
        if (elapsedSeconds <= 0.seconds) return
        val distance = (nextSec - prevSec)
        val lifeTime = (distance / bpmToSec)
        if (elapsedSeconds >= nextSec - lifeTime) {
            val angle = performAngle(nextSec)
            val ghost = createGhostNote(angle, lifeTime, nextSec)
            lives.add(ghost)
            screen.dispatch(GhostSpawnEvent(ghost))
            previousNote = currentNote
            currentNote += noteIterator.next()
            noteCounter++
        }
    }
}
