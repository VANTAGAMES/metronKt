import effect.*
import korlibs.audio.sound.*
import korlibs.image.font.*
import korlibs.io.file.std.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.ColorUtil.hex
import kotlin.math.*

class State(
    val level: Level,
    val container: Container,
    var magnanimity: Double = level.magnanimity,
) {
    val delay: Double = bpmToSec*4.0
    val bpm get() = level.bpm
    val bpmToSec get() = level.bpmToSec
    var easing: Easing = getDefaultEasing()
    val degrees: Angle = level.degrees.degrees
    val note: Note = note()
    lateinit var livingStick: LivingStick
    lateinit var music: Sound
    lateinit var hitSound: Sound
    lateinit var boldFont: Font
    lateinit var mediumFont: Font

    private fun getDefaultEasing() = Easing {
        (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f
    }

}

class Stage(private val level: Level) : Scene() {
    override suspend fun SContainer.sceneMain() {
        State(level, Container().addTo(containerRoot)).apply {
            boldFont = resourcesVfs["fonts/NanumSquareNeoTTF-eHv.woff"].readWoffFont()
            mediumFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
            val audioStream = resourcesVfs["sounds/hit.wav"].readMusic().toStream()
            hitSound = nativeSoundProvider.createStreamingSound(audioStream)

            livingStick = LivingStick(container.note(ColorPalette.stick.hex()) { zIndex = 1f }, note.stickAngle)
            blur(from = 50f, to = 0f, easing = Easing.EASE_OUT, period = 0.5.seconds) {
                container.filter = null
            }


            spawnAudit(dummyView(), Audit.PERF)
//            music = resourcesVfs["song.ogg"].readMusic()
            welcomeText()
            registerInput()
//            val livingGhost = LivingGhost(this, 0.degrees, 10.seconds, 1.seconds).stick
        }
    }
}

suspend fun State.reloadStage() {
    container.removeFromParent()
    sceneContainer.changeTo<Stage>(Stage(this@reloadStage.level))

}
