import event.*
import korlibs.datastructure.*
import korlibs.datastructure.iterators.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import util.*
import util.ColorUtil.hex

fun State.note(stick: StickAngle) = Note(stick, this)

class LivingGhost(
    val stick: Stick,
    val note: TimeSpan
)
class Note(
    val stickAngle: StickAngle,
    val state: State,
    val alives: FastArrayList<LivingGhost> = fastArrayListOf(),
    view: View = DummyView().addTo(state.container)
) {
    val iter = state.level.map.iterator()
    var prev = .5
    var curr = .0
    private val ghostStick = state.run {
        StickAngle(degrees, bpm, easing, offset = (-0.75).seconds)
    }

    init {
        view.onEvent(UpdateEvent) {
            ghostStick.update(it.deltaTime)
            if (iter.hasNext()) {
                val nextSec = curr.seconds * state.bpmToSec
                val prevSec = prev.seconds * state.bpmToSec
                val prevAngle = ghostStick.performAngle(prevSec)
                val angle = ghostStick.performAngle(nextSec)
//            println("note=$angle, stick=${stick.performAngle().degrees}")
                val distance = angle.absBetween180degrees(stickAngle.performAngle())
                val length = prevAngle.absBetween180degrees(angle)
//                println("distance=$distance, length=$length")
                if (distance <= length / 2) {
                    val lifeTime = 1.seconds
                    val ghost = state.spawnGhost(angle, lifeTime)
                    alives.add(LivingGhost(ghost, curr.seconds))
                    state.container.dispatch(GhostSpawnEvent(angle, lifeTime, ghost))
                    prev = curr
                    curr += iter.next()
                }
            }
        }
    }

    private fun State.spawnGhost(angle: Angle, lifeTime: TimeSpan) = container.note("394867".hex()) {
        rotation = angle
        zIndex = 0f
        var elapsed = 0.milliseconds
        onEvent(UpdateEvent) {
            elapsed += it.deltaTime
            if (elapsed > lifeTime * bpmToSec) {
                removeFromParent()
                alives.fastIterateRemove { it.stick == this }
            }
        }
    }
}
