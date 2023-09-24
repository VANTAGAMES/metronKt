package metron.app.components

import event.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.util.Effect
import metron.util.Effect.Companion.easingEffect

fun Stage.enableHitEffect() {
    val dummyView = screen.dummyView()
    screen.onEvent(HitEvent) {
        dummyView.easingEffect(0.05.seconds, Easing.LINEAR, arrayOf(
            Effect { _, value -> camera.scale(1 + (1 - value) * 0.01); camera.centerOn(screen) }
        ))
    }
}
