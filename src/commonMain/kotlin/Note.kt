import korlibs.datastructure.*
import korlibs.korge.view.*
import korlibs.time.*

fun State.note() = Note(this)

class Note(
    val state: State,
    val alives: FastArrayList<LivingGhost> = fastArrayListOf(),
    val view: View = DummyView().addTo(state.container)
) {
    val stickAngle: StickAngle = state.run {
        StickAngle(degrees, bpm, easing, - (delay).seconds)
    }
    val iter = state.level.map.iterator()
    var prev = 0.0
    var curr = 0.5
    val ghostStick = state.run {
        StickAngle(degrees, bpm, easing, - (delay).seconds)
    }
}
