package metron.app.components

import event.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.effectPosY

class Combo(val stage: Stage) {
    private var viewOrNull: View? = null
    val view: View
        get() = viewOrNull?: run {
            viewOrNull = stage.createTitle("", fontSize = 30) {
                positionY(pos.y - 70f)
            }
            viewOrNull!!
        }

    private var stack: Int = 0
        set(value) {
            field = value
            if (value > 0) viewOrNull.setText(formattedCombo(value))
        }

    fun stepCombo() {
        if (stack == 1) {
            showCombo()
        }
        stack++
    }
    fun resetCombo() {
        hideCombo()
        stack = 0
    }

    private fun showCombo() {
        val span = 0.7.seconds
        viewOrNull = null
        view.easingEffect(
            span, Easing.EASE_OUT, arrayOf(
                effectAlpha(1.2f),
                effectPosY(span.seconds.toFloat() * 100f)
            )
        )
    }
    private fun hideCombo() {
        val span = 0.7.seconds
        viewOrNull?.easingEffect(
            span, Easing.EASE_IN, arrayOf(
                effectAlpha(1.2f, isDown = true),
                effectPosY(span.seconds.toFloat() * 100f)
            )
        ) {
            removeFromParent()
        }.also {
            viewOrNull = null
        }
    }


}


fun Stage.enableCombo() {
    Combo(this).apply {
        screen.onEvent(GhostDrawedEvent) {
            if (it.isNaturally) resetCombo()
        }
        screen.onEvent(AuditEvent) {
            if (it.audit == AuditType.PERF) stepCombo() else resetCombo()
        }
    }
}

private fun formattedCombo(combo: Int) = " ${combo - 1} \n-COMBO- "

