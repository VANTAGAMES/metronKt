package effect

import State
import korlibs.korge.view.*
import korlibs.math.geom.*
import sceneContainer

fun State.containerRatio() {
    container.onStageResized { width, height ->
        container.scale(height/sceneContainer.height, height/sceneContainer.height)

        container.positionX(sceneContainer.width/2)
        container.positionY((sceneContainer.scaledHeight - height)/2f)
    }
}
