import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.ColorUtil.hex
import kotlin.math.*

//9BA4B5
//212A3E
//394867
//F1F6F9

class State(
    val level: Level,
    val container: Container,
    var magnanimity: Double = level.magnanimity,
    val delay: Int = 3
) {
    val bpm get() = level.bpm
    var easing: Easing = getDefaultEasing()
    val degrees: Angle = level.degrees.degrees
    val note: Note = note(StickAngle(degrees, bpm, easing, bpmToSec.seconds/2))
    lateinit var livingStick: LivingStick
    val bpmToSec get() = 60.0 / bpm

    private fun getDefaultEasing() = Easing {
        (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f
    }

}
class Stage(private val level: Level) : Scene() {
    override suspend fun SContainer.sceneMain() {
        State(level, Container().addTo(containerRoot)).apply {
            livingStick = LivingStick(container.note("9BA4B5".hex()) { zIndex = 1f }, note.stickAngle)
            welcomeText()
        }
    }
}
