import korlibs.datastructure.*
import korlibs.korge.view.*
import korlibs.time.*

fun State.note(stick: StickAngle) = Note(stick, this)

class Note(
    val stickAngle: StickAngle,
    val state: State,
    val alives: FastArrayList<LivingGhost> = fastArrayListOf(),
    val view: View = DummyView().addTo(state.container)
) {
    val iter = state.level.map.iterator()
    var prev = 0.0
    var curr = 0.5
    val ghostStick = state.run {
        StickAngle(degrees, bpm, easing, - (delay).seconds*bpmToSec*2)
    }
}
