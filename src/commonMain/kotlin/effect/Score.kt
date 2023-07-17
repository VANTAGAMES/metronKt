package effect

import State
import event.*
import korlibs.image.text.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.korge.view.filter.*
import korlibs.math.geom.*
import util.ColorUtil.hex

fun State.score() {
    val scoreTxt =
    screenContainer.textBlock(
        RichTextData(" 레벨 1 클리어 ", color = ColorPalette.text.hex(), textSize = 80f, font = boldFont),
        size = Size(1000, 95),
        align = TextAlignment.MIDDLE_CENTER
    ) {
        filter = IdentityFilter
        centerXOnStage()
        visible = false
        alignY(root, 0.35, true)
    }
    val originY = scoreTxt.y
    container.dummyView().apply {
        onEvent(GameEndEvent) {
            scoreTxt.positionY(originY)
            showUpThis(scoreTxt) {}
        }
        onEvent(GameDisposeEvent) {
            hideIt(scoreTxt) {
                scoreTxt.positionY(originY)
            }
        }
    }

}
