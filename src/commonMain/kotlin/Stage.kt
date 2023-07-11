import korlibs.audio.sound.*
import korlibs.datastructure.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import util.ColorUtil.hex
import kotlin.coroutines.*
import kotlin.math.*

class State(
    val level: Level,
    val container: Container,
    var magnanimity: Double = level.magnanimity,
) {
    val delay: Int = (bpmToSec*4).seconds.seconds.toInt()
    val bpm get() = level.bpm
    var easing: Easing = getDefaultEasing()
    val degrees: Angle = level.degrees.degrees
    val note: Note = note(StickAngle(degrees, bpm, easing, this, - (delay).seconds*bpmToSec*2))
    lateinit var livingStick: LivingStick
    val bpmToSec get() = 60.0 / bpm
    lateinit var music: Sound
    lateinit var hitSound: Sound

    private fun getDefaultEasing() = Easing {
//        Easing.EASE_IN_OUT.invoke(it)
        (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f
    }

}

val soundQueue = IntStack()

class Stage(private val level: Level) : Scene() {
    override suspend fun SContainer.sceneMain() {
        State(level, Container().addTo(containerRoot)).apply {
            val audioStream = resourcesVfs["hit.wav"].readMusic().toStream()
            hitSound = nativeSoundProvider.createStreamingSound(audioStream)

            spawnAudit(dummyView(), Audit.PERF)

//            music = resourcesVfs["song.ogg"].readMusic()
            livingStick = LivingStick(container.note(ColorPalette.stick.hex()) { zIndex = 1f }, note.stickAngle)
            welcomeText()
            registerInput()
//            val livingGhost = LivingGhost(this, 0.degrees, 10.seconds, 1.seconds).stick
        }
    }
}

