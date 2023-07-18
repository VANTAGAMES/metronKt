import effect.*
import event.*
import korlibs.audio.format.*
import korlibs.audio.sound.*
import korlibs.image.font.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import util.ColorUtil.hex
import kotlin.coroutines.*
import kotlin.math.*

class State(
    rootContainer: Container,
    val level: Level,
    var magnanimity: Double = .0,
) {
    lateinit var version: String
    val screenContainer: Container = Container().addTo(rootContainer)
    val container: Container = Container().addTo(screenContainer)
    val offset = level.offset
    val offsetToSec = offset*bpmToSec
    val initialNote = level.initialNote
    val delay: Double = bpmToSec*4.0
    val bpm get() = level.bpm
    val bpmToSec get() = level.bpmToSec
    var easing: Easing = getDefaultEasing()
    val degrees: Angle = level.degrees.degrees
    var note: Note = note()
    var isPaused: Boolean = false
    lateinit var stage: Stage
    lateinit var music: Sound
    var playingMusic: SoundChannel? = null
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
        State(containerRoot, level).apply {
            version = resourcesVfs["client.properties"].readProperties()["version"]!!
            val debugTextSize = 20f
            sceneContainer.text(version, textSize = debugTextSize, color = ColorPalette.text.hex())
            containerRatio()
            currentCoroutineContext = currentCoroutineContext()
            stage = this@Stage
            hitSound = resourcesVfs["sounds/hit.wav"].apply { cachedToMemory() }.readSound()
            music = resourcesVfs["levels/song.mp3"].apply { cachedToMemory() } .readSound()
            music.apply { play().apply { volume = .0 } }
            hitSound.apply { play().apply { volume = .0 } }
            boldFont = resourcesVfs["fonts/NanumSquareNeoTTF-eHv.woff"].readWoffFont()
            mediumFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()

            livingStick()
            container.onEvent(GameEndEvent) {
                playingMusic?.stop()
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

        screenContainer.removeFromParent()
        sceneContainer.changeTo<Stage> { Stage(this@reloadStage.level) }
    }
}
