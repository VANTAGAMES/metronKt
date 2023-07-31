import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.geom.abs
import korlibs.time.*
import kotlin.math.*

fun State.autoMacro() {
    container.onEvent(UpdateEvent) {
        if (isPaused) return@onEvent
//        println("asdf")
        if (note.stickAngle.elapsed <= 0.seconds) {
            return@onEvent
        }
//        println(note.stickAngle.elapsed - note.curr.seconds*bpmToSec)
        val defaultElapsed = note.stickAngle.defaultElapsed()
        val dis = abs((note.stickAngle.elapsed - note.curr.seconds * bpmToSec).seconds)
        if (dis < 0.35) {
            audit(delta = 0.seconds)
        }
    }
}
