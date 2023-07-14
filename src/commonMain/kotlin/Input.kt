import effect.*
import event.*
import korlibs.event.*
import korlibs.io.async.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.time.*
import kotlinx.coroutines.*

fun State.registerInput() {
    container.apply {
        keys {
            down(Key.SPACE) { hit(it.deltaTime) }
            down(Key.ESCAPE) {
                isPaused = !isPaused
                if (isPaused) {
                    playingMusic?.pause()
                } else {
                    playingMusic?.resume()
                }
            }
        }
        onEvent(TouchEvent.Type.START) { hit(it.currentTime - DateTime.now()) }
    }
}

private fun State.hit(delta: TimeSpan) {
    screenContainer.dispatch(HitEvent(delta))
}
