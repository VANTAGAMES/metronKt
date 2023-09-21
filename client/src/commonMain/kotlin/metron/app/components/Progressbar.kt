package metron.app.components

import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import metron.*
import metron.app.Stage
import util.*

const val progressbarThickness = 10f
fun Stage.createProgressbar() {
    screen.solidRect(Size()).transform {
        size(screen.width, progressbarThickness)
        scaleX = 0f
        positionY(screen.height - progressbarThickness)
    }.addUpdater {
        scaleX = elapsedSeconds / playingTime
    }
}
