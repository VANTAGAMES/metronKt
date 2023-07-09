import korlibs.datastructure.*
import korlibs.korge.view.*
import korlibs.time.*

fun State.note(stick: StickAngle) = Note(stick, this)

class LivingGhost(
    val stick: Stick,
    val note: TimeSpan
)
class Note(
    val stickAngle: StickAngle,
    val state: State,
    val alives: FastArrayList<LivingGhost> = fastArrayListOf(),
    val view: View = DummyView().addTo(state.container)
) {
    val iter = state.level.map.iterator()
    var prev = state.bpmToSec/2
    var curr = state.bpmToSec
    val ghostStick = state.run {
        StickAngle(degrees, bpm, easing, state, bpmToSec.seconds/2)
    }
}
