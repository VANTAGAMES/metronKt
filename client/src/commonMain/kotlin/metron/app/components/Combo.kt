package metron.app.components

import event.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.interpolation.*
import korlibs.time.*
import metron.*
import metron.app.*
import metron.app.Stage
import metron.util.Effect.Companion.easingEffect
import metron.util.Effect.Companion.effectAlpha
import metron.util.Effect.Companion.effectPosY
import util.*

fun Stage.enableCombo() {
    Combo(this).apply {
        screen.onEvent(GhostDrawedEvent) {
            if (!it.ghostStick.isHitted && it.isNaturally) resetCombo()
        }
        screen.onEvent(AuditEvent) {
            if (it.audit == AuditType.PERF) stepCombo() else resetCombo()
        }
        screen.onEvent(GameEndEvent) {
            resetCombo()
        }
    }
}

class Combo(val stage: Stage) {
    private var viewOrNull: View? = null
    private val span get() = stage.bpmToSec.seconds/6
    val view: View
        get() = viewOrNull?: run {
            viewOrNull = stage.createTitle("", fontSize = 30) {
                transform {
                    centerXOn(screen)
                    positionY(pos.y - span.seconds*100)
                }
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
        viewOrNull = null
        view.easingEffect(
            span, Easing.EASE_OUT, arrayOf(
                effectAlpha(1.2f),
                effectPosY(span.seconds.toFloat() * 100f)
            )
        )
    }
    private fun hideCombo() {
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


    private fun formattedCombo(combo: Int) = " ${combo - 1} \n-COMBO- "
}


