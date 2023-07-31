import effect.*
import korlibs.event.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.geom.abs
import korlibs.time.*
import kotlin.math.*

fun State.autoMacro() {
    val txt = txtWithFilter("자동 플레이") {}.alignX(sceneContainer, 2.0, true)
        .visible(false)
    container.keys {
        down(Key.P) {
            txt.visible = !txt.visible
        }

    }
    container.onEvent(UpdateEvent) {
        if (isPaused) return@onEvent
        if (!txt.visible) return@onEvent
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
