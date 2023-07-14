import korlibs.korge.view.*
import korlibs.time.*

fun State.autoMacro() {
    container.onEvent(UpdateEvent) {
        if (isPaused) return@onEvent
//        println("asdf")
        if (note.stickAngle.elapsed <= 0.seconds) {
            return@onEvent
        }
//        println(note.stickAngle.elapsed - note.curr.seconds*bpmToSec)
        if (note.stickAngle.elapsed >= note.curr.seconds) {
            audit(delta = 0.seconds)
        }
    }
}
