package metron.app.handlers

import event.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import metron.*
import metron.app.*
import metron.app.Stage
import util.*

fun Stage.enableAutoPlaying() {
    val body = screen.uiText("Auto") {
        styles {
            textFont = mediumFont
            textColor = globalTextColor
            textSize = 70f
            textAlignment = TextAlignment.MIDDLE_RIGHT
        }
        transform {
            val padding = 150
            positionX(screen.width - width - padding)
            positionY(padding)
        }
    }
    screen.addUpdater {
        body.visible = autoPlaying
        if (!autoPlaying) return@addUpdater
        val d = currentNote + it.seconds - elapsedSeconds.seconds / bpmToSec
        if (d-(currentNote-previousNote) in AuditType.PERF.range) {
            screen.dispatch(HitEvent(it))
        }
    }
}
