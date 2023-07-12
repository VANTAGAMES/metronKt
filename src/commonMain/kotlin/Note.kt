import korlibs.datastructure.*
import korlibs.korge.view.*
import korlibs.time.*

fun State.note(view: View? = null) = if (view === null) Note(this) else Note(this, view)

class Note(
    val state: State,
    val view: View = DummyView().addTo(state.container)
) {
    val alives: FastArrayList<LivingGhost> = fastArrayListOf()
    val stickAngle: StickAngle = state.run {
        StickAngle(degrees, bpm, easing, state)
    }
    var count = 0
    val iter = state.level.map.iterator()
    var prev = 0.0
    var curr = 0.5
    val ghostStick = state.run {
        StickAngle(degrees, bpm, easing, state)
    }
}
