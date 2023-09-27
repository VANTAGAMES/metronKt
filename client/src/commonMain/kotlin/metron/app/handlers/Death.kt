package metron.app.handlers

import event.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.app.components.*
import metron.util.*
import metron.util.Effect.Companion.easingEffect
import util.*
import kotlin.math.*

val deathGageHeight get() = stickHeight

class HealthBar(val stage: Stage, val view: View)

fun Stage.enableDeath() {
    screen.container {
        fastRoundRect(Size(stickWidth, deathGageHeight), RectCorners(1), PlayerStick.baseBodyColor)
        fastRoundRect(Size(stickWidth, deathGageHeight), RectCorners(1), globalTextColor) {
            val stage = this@enableDeath
            val healthBar = HealthBar(stage, this)
            screen.onEvent(GameStartEvent) {
                if (height != deathGageHeight)
                    screen.dummyView().easingEffect(bpmToSec.seconds/4, Easing.SMOOTH,
                        arrayOf(Effect { _, value -> height = value* deathGageHeight }))
            }
            scaleY = -1f
            positionY(pos.y + height)
            addUpdater {
                healthBar.view.visible = !isEditingMap
                if (isEditingMap) return@addUpdater
                if (isPaused) return@addUpdater
                if (elapsedSeconds < 0.seconds) return@addUpdater
                healthBar.modifyHealth(-0.98f)
            }
            screen.onEvent(GhostDrawedEvent) {
                if (isEditingMap) return@onEvent
                healthBar.decreaseHealth(10f)
            }
            screen.onEvent(AuditEvent) {
                if (isEditingMap) return@onEvent
                healthBar.decreaseHealth(
                    when (it.audit) {
                        AuditType.TOO_FAST -> 20f
                        AuditType.TOO_SLOW -> 20f
                        AuditType.FAST -> 10f
                        AuditType.SLOW -> 10f
                        AuditType.PERF -> -15f
                    }
                )
            }
        }
        transform {
            val padding = 60
            alignY(screen, 0.48, true)
            positionX(padding)
        }
    }
}

private fun HealthBar.decreaseHealth(amount: Float): Unit = view.run {
    screen.dummyView().easingEffect(0.1.seconds, Easing.EASE_OUT, arrayOf(Effect { view, _ ->
        modifyHealth(-amount)
    }))
}

private fun HealthBar.modifyHealth(adder: Float): Unit = view.run {
    if (this@modifyHealth.stage.isStopped) return@run
    val newValue = height + adder
    height = min(newValue, deathGageHeight)
    if (height <= 0) {
        screen.dispatch(GameEndEvent(isSuccess = false))
    }
}
