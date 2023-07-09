import event.*
import korlibs.audio.sound.*
import korlibs.datastructure.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
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
    val note: Note = note(StickAngle(degrees, bpm, easing, this, bpmToSec.seconds/2 - (delay).seconds))
    lateinit var livingStick: LivingStick
    val bpmToSec get() = 60.0 / bpm
    lateinit var music: Sound
    lateinit var hitSound: Sound

    private fun getDefaultEasing() = Easing {
        (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f
    }

}

val soundQueue = IntStack()

class Stage(private val level: Level) : Scene() {
    override suspend fun SContainer.sceneMain() {
        val state = State(level, Container().addTo(containerRoot)).apply {
            hitSound = resourcesVfs["hit.wav"].readMusic()
//            music = resourcesVfs["song.ogg"].readMusic()
            livingStick = LivingStick(container.note(ColorPalette.stick.hex()) { zIndex = 1f }, note.stickAngle)
            welcomeText()
        }
        onEvent(UpdateEvent) {
            if (soundQueue.isNotEmpty()) {
                soundQueue.clear()
                async { state.hitSound.play() }
            }
        }
    }
}
