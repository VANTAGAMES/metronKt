import event.*
import korlibs.image.text.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.time.*
import util.ColorUtil.hex

fun State.combo() {
    var combo = 0
    var lastComboUpdated = DateTime.now()
    fun formattedCombo() = " $combo \n-COMBO- "
    container.textBlock(
        RichTextData(formattedCombo(), color = ColorPalette.text.hex(), textSize = 40f),
        size = Size(1000, 95),
        align = TextAlignment.MIDDLE_CENTER
    ) {
        centerXOnStage()
        alignY(root, 0.05, true)
        container.onEvent(GhostDrawedEvent) {
            if (it.isNaturally) {
                if (lastComboUpdated > it.livingGhost.startTime) {
                    return@onEvent
                }
                combo = 0
                lastComboUpdated = DateTime.now()
                plainText = formattedCombo()
            }
        }
        onEvent(AuditEvent) {
            if (it.audit == Audit.PERF) combo += 1 else combo = 0
            lastComboUpdated = DateTime.now()
            plainText = formattedCombo()
        }
        showUpThis {

        }
    }
}
