import event.*
import korlibs.datastructure.iterators.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import util.*
import util.ColorUtil.hex

fun State.ghostSpawner(): Unit = note.run {
    view.onEvent(UpdateEvent) {
        ghostStick.update(it.deltaTime)
        if (iter.hasNext()) {
            val nextSec = curr.seconds * state.bpmToSec
            val prevSec = prev.seconds * state.bpmToSec
//            if (nextSec - ghostStick.elapsed > state.bpmToSec.seconds/2) return@onEvent
            val prevAngle = ghostStick.performAngle(prevSec)
            val angle = ghostStick.performAngle(nextSec)
//            println("note=$angle, stick=${stick.performAngle().degrees}")
            val distance = angle.absBetween180degrees(stickAngle.performAngle())
            val length = prevAngle.absBetween180degrees(angle)
//                println("distance=$distance, length=$length")
            if (distance <= length / 2) {
                val lifeTime = 1.seconds
                val ghost = state.spawnGhost(angle, lifeTime)
                alives.add(LivingGhost(ghost, prevSec))
                state.container.dispatch(GhostSpawnEvent(angle, lifeTime, ghost))
                prev = curr
                curr += iter.next()
            }
        }
    }
}



private fun State.spawnGhost(angle: Angle, lifeTime: TimeSpan) = note.run {
    container.note(ColorPalette.ghost.hex()) {
        rotation = angle
        zIndex = 0f
        var elapsed = 0.milliseconds
        onEvent(UpdateEvent) {
            elapsed += it.deltaTime
            if (elapsed > lifeTime * bpmToSec/2) {
                removeFromParent()
                alives.fastIterateRemove { it.stick == this }
            }
        }
    }
}
