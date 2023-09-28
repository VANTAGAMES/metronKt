package metron.app.handlers

import event.*
import korlibs.audio.sound.*
import korlibs.event.*
import korlibs.korge.input.*
import korlibs.korge.time.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.systems.AuditSpawnerSystem.Companion.audit
import kotlin.math.*

fun Stage.enableInput() = screen.apply {
    screen.onEvent(HitEvent) {
        if (elapsedSeconds < 0.seconds) return@onEvent
        audit(it.delta)
    }
    keys {
        down(Key.SPACE) { hit(it.deltaTime) }
        down(Key.A) { autoPlaying = !autoPlaying }
        down(Key.ESCAPE) {
            if (isPausedByUser) {
                isPausedByUser = false
                isSettingsMenuOpened = false
                screen.dispatch(SettingsMenuToggleEvent())
                if (elapsedSeconds >= 0.seconds) countdown {
                    if (playingMusic!!.paused) {
                        println(max(.0, offset * -1).seconds)
                        val timeSpan = elapsedSeconds - max(.0, offset * -1).seconds
                        if (timeSpan < 0.seconds) {
                            timeout(timeSpan) {
                                playingMusic!!.togglePaused()
                            }
                        } else {
                            playingMusic!!.current = timeSpan
                            playingMusic!!.togglePaused()
                        }
                    }
                }
            } else {
                isSettingsMenuOpened = true
                screen.dispatch(SettingsMenuToggleEvent())
                playingMusic?.pause()
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
