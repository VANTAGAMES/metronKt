import event.*
import korlibs.image.color.*
import korlibs.image.text.*
import korlibs.io.lang.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import util.ColorUtil.hex

fun State.combo() {
    var combo = 0
    var lastComboUpdated = DateTime.now()
    fun formattedCombo() = " $combo \n-COMBO- "
    Container().addTo(container) {
        textBlock(
            RichTextData(formattedCombo(), color = ColorPalette.text.hex(), textSize = 40f),
            size = Size(1000, 95),
            align = TextAlignment.MIDDLE_CENTER
        ) {
            visible = false
            centerXOnStage()
            alignY(root, 0.0, true)
            val originY = pos.y
            container.onEvent(GhostDrawedEvent) {
                if (it.isNaturally) {
                    if (lastComboUpdated > it.livingGhost.startTime) {
                        return@onEvent
                    }
                    combo = 0
                    lastComboUpdated = DateTime.now()
                    plainText = formattedCombo()
                    hideCombo(originY, this)
                }
            }
            onEvent(AuditEvent) {
                if (it.audit == Audit.PERF) {
                    combo += 1
//                    comboHitEffect {  }
                    if (!visible) {
                        showCombo(this)
                    }
                }
                else {
                    combo = 0
                    hideCombo(originY, this)
                }
                lastComboUpdated = DateTime.now()
                plainText = formattedCombo()
            }
        }
    }
}

fun State.hideCombo(originY: Float, container: Container) {
    if (!container.visible) return
    container.hideIt(period = bpmToSec.seconds/4) {
        container.positionY(originY)
    }
}

fun State.showCombo(container: Container) {
    container.visible = true
    container.showUpThis(period = bpmToSec.seconds/4) {
    }
}

fun View.comboHitEffect(period: TimeSpan = 1.seconds, easing: Easing = Easing.EASE_OUT_QUAD, callback: () -> Unit) {
    val startTime = DateTime.now()
    zIndex = 0f
    lateinit var listener: Cancellable
    listener = onEvent(UpdateEvent) {
        val now = DateTime.now()
        val span = now - startTime
        if (span >= period) {
            listener.cancel()
            callback()
        } else {
            val i = (1 - (span / period))/17
            val a = kotlin.math.min(1f, kotlin.math.max(0f, easing.invoke(i)))
            scale(1 + a, 1 + a)
        }
    }
}
