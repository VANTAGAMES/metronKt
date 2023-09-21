package metron.app

import korlibs.io.async.*
import korlibs.korge.time.*
import korlibs.time.*
import metron.*
import util.*
import kotlin.math.*

suspend fun Stage.enableMusic() {
    val offset = max(.0, offset * -1).seconds
    screen.timers.timeout(offset) {
        launchNow { playingMusic = music.play().apply { volume = 0.55 } }
    }
}
