package metron.app.components

import event.*
import korlibs.image.text.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import metron.*
import metron.app.*
import metron.app.Stage
import util.*

fun Stage.enableScore() {
    var score = 0
    val body = screen.uiText("0") {
        styles {
            textFont = mediumFont
            textColor = globalTextColor
            textSize = 70f
            textAlignment = TextAlignment.MIDDLE_RIGHT
        }
        transform {
            val padding = 40
            positionX(screen.width - width - padding)
            positionY(padding)
        }
    }
    screen.onEvent(AuditEvent) { event ->
        val scoreAdder = when(event.audit) {
            AuditType.TOO_FAST -> 0
            AuditType.TOO_SLOW -> 0
            AuditType.FAST -> 0
            AuditType.SLOW -> 0
            AuditType.PERF -> 1_000_000/level.map.size
        }
        if (scoreAdder != 0) {
            score += scoreAdder
            body.setText("$score")
        }
    }
}
