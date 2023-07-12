import effect.*
import korlibs.audio.sound.*
import korlibs.image.font.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.filter.*
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
    var magnanimity: Double = .0,
) {
    val delay: Double = bpmToSec*4.0
    val bpm get() = level.bpm
    val bpmToSec get() = level.bpmToSec
    var easing: Easing = getDefaultEasing()
    val degrees: Angle = level.degrees.degrees
    var note: Note = note()
    lateinit var music: Sound
    lateinit var hitSound: Sound
    lateinit var boldFont: Font
    lateinit var mediumFont: Font
    lateinit var currentCoroutineContext: CoroutineContext
    private fun getDefaultEasing() = Easing {
        (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f
    }

}

class Stage(private val level: Level) : Scene() {
    override suspend fun SContainer.sceneMain() {
        State(level, Container().addTo(containerRoot)).apply {
            currentCoroutineContext = currentCoroutineContext()
            boldFont = resourcesVfs["fonts/NanumSquareNeoTTF-eHv.woff"].readWoffFont()
            mediumFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
            val audioStream = resourcesVfs["sounds/hit.wav"].readMusic().toStream()
            hitSound = nativeSoundProvider.createStreamingSound(audioStream)
            container.note(ColorPalette.stick.hex()) {
                zIndex = 1f
                onEvent(UpdateEvent) {
                    rotation = note.stickAngle.setTo(it.deltaTime)
                }
            }
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

fun State.reloadStage() {
    launchImmediately(currentCoroutineContext) {

        container.removeFromParent()
        sceneContainer.changeTo<Stage> { Stage(this@reloadStage.level) }
    }
}
