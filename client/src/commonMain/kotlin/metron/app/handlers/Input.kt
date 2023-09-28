package metron.app.handlers

import event.*
import korlibs.audio.sound.*
import korlibs.audio.sound.fade.*
import korlibs.event.*
import korlibs.korge.input.*
import korlibs.korge.time.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.systems.AuditSpawnerSystem.Companion.audit
import metron.util.*
import util.*

fun Stage.enableInput() = screen.apply {
    screen.onEvent(HitEvent) {
        if (elapsedSeconds < 0.seconds) return@onEvent
        audit(it.delta)
    }
    var pauseByUserSemaphere = false
    keys {
        down(Key.SPACE) { hit(it.deltaTime) }
        down(Key.A) { autoPlaying = !autoPlaying }
        down(Key.ESCAPE) {
            if (elapsedSeconds < 0.seconds) return@down
            if (isPausedByUser && !pauseByUserSemaphere) {
                pauseByUserSemaphere = true
                countdown {
                    val started = DateTime.now()
                    screen.addTimer(-defaultElapsed()/60) {
                        pauseByUserSemaphere = false
                        println(DateTime.now() - started)
                        if (playingMusic!!.paused)
                            playingMusic!!.togglePaused()
                    }
                }
            } else if (playingMusic?.paused?.not() == true) {
                playingMusic!!.pause()
                isPausedByUser = true
            }
            if (isEditingMap) {
                screen.dispatch(GameEndEvent(isSuccess = false))
            }
        }
        down(Key.E) {
            enableMapEditor()
        }
    }
    screen.onEvent(TouchEvent.Type.START) { hit(it.currentTime - DateTime.now()) }
}

private fun hit(delta: TimeSpan) {
    screen.dispatch(HitEvent(delta))
}
