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
                container.removeFromParent()
                sceneContainer.changeTo<Stage>() { Stage(this@registerInput.level) }
            }
        }
        onEvent(TouchEvent.Type.START) { hit(it.currentTime - DateTime.now()) }
    }
}

private fun Container.hit(delta: TimeSpan) {
    dispatch(HitEvent(delta))
}
