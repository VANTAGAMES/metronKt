package metron.app

import com.github.quillraven.fleks.*
import korlibs.audio.sound.*
import korlibs.datastructure.*
import korlibs.image.font.*
import korlibs.io.file.std.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.serialization.json.*
import metron.*
import metron.app.components.*
import kotlin.math.*

data class Stage(
    val level: Level,
    val music: Sound,
    var hitSound: Sound,
    val boldFont: Font,
    val mediumFont: Font,
) {
    var worldOrNull: World? = null
    var world: World
        get() = worldOrNull!!
        set(value) { worldOrNull = value }

    val angle: Angle = level.degrees.degrees
    val bpm get() = level.bpm
    val bpmToSec get() = level.bpmToSec
    val initialNote get() = level.initialNote
    val delay get() = bpmToSec*4.0
    val offset get() = level.offset
    val offsetToSec get() = offset*bpmToSec
    val degrees get() = level.degrees.degrees
    val easing: Easing = getDefaultEasing()
    var playingMusic: SoundChannel? = null

    var isForcePaused: Boolean = true
    var isPausedByUser: Boolean = false
    val isPaused get() = isForcePaused || isPausedByUser
    var magnanimity = .0
    var elapsedSeconds: TimeSpan = defaultElapsed()
    val alives: MutableList<GhostStick> = fastArrayListOf()
    var noteCounter = 0
    var noteIterator = level.map.iterator()
    var previousNote = .0
    var currentNote = initialNote

    fun defaultElapsed() =
        - (delay.seconds + max(0.seconds, offsetToSec.seconds))
    private fun getDefaultEasing() =
        Easing { (((cos(PI * it) + 1) / 2) * magnanimity - 0.5 * magnanimity).toFloat() + 0.5f }

    fun resetElapsed() {
        elapsedSeconds = defaultElapsed()
    }

    fun update(delta: TimeSpan) {
        elapsedSeconds += delta
    }
    fun setTo(delta: TimeSpan): Angle {
        update(delta)
        return performAngle()
    }

    fun performAngle(elapsed: TimeSpan = this.elapsedSeconds): Angle {
        val durationInTween = (60.0 / bpm).seconds
        val elapsedInTween = run {
            val sum = elapsed % (durationInTween*2)
            if (sum > durationInTween) durationInTween*2 - sum else sum
        }
        val ratioInTween = elapsedInTween / durationInTween
        val easedRatioInTween = easing(ratioInTween)
        val ratio = easedRatioInTween.toRatio()
        return ratio.interpolateAngleNormalized(-angle, angle)
    }

    companion object {
        suspend operator fun invoke(levelName: String, configuration: WorldConfiguration.(Stage) -> Unit): Stage {
            val hitSound = nativeSoundProvider.createNonStreamingSound(
                resourcesVfs["sounds/hit.wav"].apply { cachedToMemory() }.readAudioData()
            ).also { it.volume = 0.7 }
            val music = resourcesVfs["$levelName/song.mp3"].apply { cachedToMemory() } .readSound()
            music.apply { play().apply { volume = .0 } }
            hitSound.apply { play().apply { volume = .0 } }
            val boldFont = resourcesVfs["fonts/NanumSquareNeoTTF-eHv.woff"].readWoffFont()
            val mediumFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
            val level = Json.decodeFromString<Level>(resourcesVfs["$levelName/level.json"].readString())
            return Stage(
                music = music,
                hitSound = hitSound,
                boldFont = boldFont,
                mediumFont = mediumFont,
                level = level
            ).also {
                it.world = configureWorld { configuration(this, it) }
                    .also { world ->
                        screen.addUpdater { deltaTime ->
                            world.update(deltaTime.seconds.toFloat())
                        }
                    }
            }
        }
    }
}
