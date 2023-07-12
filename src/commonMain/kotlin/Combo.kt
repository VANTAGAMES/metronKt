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
            RichTextData(formattedCombo(), color = ColorPalette.text.hex(), textSize = 40f, font = boldFont),
            size = Size(1000, 95),
            align = TextAlignment.MIDDLE_CENTER,
        ) {
            visible = false
            centerXOnStage()
            alignY(root, 0.025, true)
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
    container.hideIt(period = bpmToSec.seconds/6) {
        container.positionY(originY)
    }
}

fun State.showCombo(container: Container) {
    container.visible = true
    container.showUpThis(period = bpmToSec.seconds/6) {
    }
}
