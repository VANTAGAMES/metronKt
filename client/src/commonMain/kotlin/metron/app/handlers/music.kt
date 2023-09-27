package metron.app.handlers

import event.*
import korlibs.korge.time.*
import korlibs.time.*
import metron.*
import metron.app.*
import util.*
import kotlin.math.*

suspend fun Stage.enableMusic() {
    val offset = max(.0, offset * -1).seconds
    screen.onEvent(GameStartEvent) {
        screen.timers.timeout(offset) {
            launchNow { playingMusic = music.play().apply { volume = 0.55 } }
        }
    }
    screen.onEvent(GameEndEvent) {
        playingMusic?.stop()
    }
}
