import event.*
import korlibs.event.*
import korlibs.io.async.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import kotlinx.coroutines.*

fun State.registerInput() {
    container.containerRoot.apply {
        keys {
            down(Key.SPACE) { hit() }
            down(Key.R) {
                container.removeFromParent()
                sceneContainer.changeTo<Stage>() { Stage(this@registerInput.level) }
            }
        }
        onEvent(TouchEvent.Type.START) { hit() }
    }
}

private fun Container.hit() {
    dispatch(HitEvent())
}
