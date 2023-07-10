import event.*
import korlibs.event.*
import korlibs.korge.input.*
import korlibs.korge.view.*

fun State.registerInput() {
    container.apply {
        keys {
            justDown(Key.SPACE) { hit() }
        }
        onClick { hit() }
        mouse {
            click { hit() }
            down { hit() }
        }
        onEvent(TouchEvent.Type.START) { hit() }
    }

}

private fun Container.hit() {
    println("ASFSD")
    dispatch(HitEvent())
}
