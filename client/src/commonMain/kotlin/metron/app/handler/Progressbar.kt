package metron.app.handler

import event.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import metron.*
import metron.app.Stage
import util.*

const val progressbarThickness = 15f
fun Stage.enableProgressbar() {
    screen.solidRect(Size()).transform {
        size(screen.width, progressbarThickness)
        scaleX = 0f
        positionY(screen.height - progressbarThickness)
    }.addUpdater {
        if (isStopped) return@addUpdater
        scaleX = elapsedSeconds / playingTime
        if (scaleX >= 1) {
            screen.dispatch(GameEndEvent(isSuccess = true))
        }
    }
}
