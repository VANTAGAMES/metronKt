import korlibs.io.async.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.time.*
import kotlin.coroutines.*
import kotlin.math.*

fun State.musicPlayer() {
    lateinit var cancellable: Cancellable
    var elapsed = 0.seconds
    cancellable = container.onEvent(UpdateEvent) {
        elapsed += it.deltaTime
        val offset = max(.0, offset * -1).seconds
        if (elapsed < offset) return@onEvent
        cancellable.cancel()
        launchImmediately(currentCoroutineContext) {
            playingMusic = music.play()
        }
    }
}
