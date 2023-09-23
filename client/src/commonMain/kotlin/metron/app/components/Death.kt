package metron.app.components

import event.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.util.Effect
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectPosY
import util.*
import kotlin.math.*

val deathGageHeight get() = stickHeight

fun Stage.enableDeath() {
    screen.fastRoundRect(Size(stickWidth, deathGageHeight), RectCorners(1), color = PlayerStick.baseBodyColor) {
        transform {
            val padding = 65
            alignY(screen, 0.48, true)
            positionX(padding)
        }
    }
    screen.fastRoundRect(Size(stickWidth, deathGageHeight), RectCorners(1), color = globalTextColor) {
        transform {
            scaleY = -1f
            val padding = 65
            alignY(screen, 0.48, false)
            positionX(padding)
        }
        addUpdater {
            if (isPaused) return@addUpdater
            if (elapsedSeconds < 0.seconds) return@addUpdater
            val newValue = height-0.98f
            height = min(newValue, deathGageHeight)
        }
        onEvent(GhostDrawedEvent) {
            decreaseHealth(15f)
        }
        onEvent(AuditEvent) {
            decreaseHealth(when (it.audit) {
                AuditType.TOO_FAST -> 20f
                AuditType.TOO_SLOW -> 20f
                AuditType.FAST -> 15f
                AuditType.SLOW -> 15f
                AuditType.PERF -> -10f
            })
        }
    }
}

private fun View.decreaseHealth(amount: Float) {
    easingEffect(0.1.seconds, Easing.EASE_OUT, arrayOf(Effect { view, _ ->
        val newValue = view.height-amount
        view.height = min(newValue, deathGageHeight)
    }))
}
