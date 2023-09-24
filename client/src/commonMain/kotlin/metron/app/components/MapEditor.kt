package metron.app.components

import event.*
import korlibs.image.text.*
import korlibs.io.file.std.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.memory.*
import korlibs.time.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import metron.*
import metron.app.Stage
import metron.app.entities.*
import util.*

suspend fun Stage.enableMapEditor() {
    screen.onEvent(GameStartEvent) {
        isEditingMap = true
        noteIterator = mutableListOf<Double>().iterator()
    }
    val debugger = screen.uiText("") {
        styles {
            textSize = 40f
            font = scene.views.debugBmpFont
            textAlignment = TextAlignment.MIDDLE_RIGHT
        }
        transform { position(screen.width-width*2, screen.height-height*2) }
    }
    var before = 0.seconds
    val map = mutableListOf<Double>()
    screen.onEvent(HitEvent) {
        if (elapsedSeconds < 0.seconds) return@onEvent
        val currentNote = ((elapsedSeconds+it.delta) / bpmToSec).seconds
        val previousNote = (before / bpmToSec).seconds
        val distance = currentNote - previousNote
        println(distance)
        createGhostNote(performAngle(roundRange(currentNote).seconds*bpmToSec), bpmToSec.seconds, 1.seconds)
        val note = roundRange(distance)
        debugger.setText(note.toString())
        before = elapsedSeconds
        map.add(note)
    }
    screen.onEvent(GameEndEvent) {
        launchNow {
            rootLocalVfs["${DateTime.now().format(DateFormat.DEFAULT_FORMAT)}.json"].writeString(
                Json.encodeToString(level.copy(map = map))
            )
        }
    }
}

fun roundRange(value: Double): Double {
    val range = 0.5
    val allowedValues = DoubleArray((1.0 / range).toInt()+1) { (it) * range }.toMutableList()
    val halfRange = range / 2.0
    val decimal = value % 1.0
    val rounded = allowedValues.first { it - halfRange <= decimal && decimal <= it + halfRange }
    return value.toIntFloor() + rounded
}

