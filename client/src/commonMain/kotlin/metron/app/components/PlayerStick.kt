package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.geom.shape.*
import metron.*
import metron.app.Stage
import metron.app.handlers.*
import metron.util.*
import util.*
import kotlin.math.*

fun Stage.createPlayerStick() = world.entity {
    val stage = this@createPlayerStick
    it += PlayerStick(stage)
}

class PlayerStick(val stage: Stage) : Component<PlayerStick> {
    lateinit var body: View
    override fun type() = Companion

    companion object : ComponentHooks<PlayerStick>() {
        val playerStickColor = Colors["FFFFFF"]
        val baseBodyColor = Colors["8e8e8e"]
        val upperBaseBodyColor = Colors["c8c8c8"]
        val metronomeHeight get() = stickHeight *159/160
        val baseUpperWidth get() = metronomeHeight/4
        val baseWidth get() = baseUpperWidth*2
        val roofHeight get() = -(metronomeHeight/20)
        val ratio get() = 1/7f
        val bottomBaseHeight get() = metronomeHeight*ratio
        val side get() = sqrt(((baseWidth - baseUpperWidth) / 2f).pow(2) + metronomeHeight.pow(2))
        val outline get() = sqrt((side * ratio).pow(2) - bottomBaseHeight.pow(2))
        val bottomBaseWidth get() = baseWidth - outline *2f
        override val onAdded: ComponentHook<PlayerStick> = { entity, playerStick ->

            arrayOf(
                camera.container {
                    shapeView().transform {
                            path = pointArrayListOf(
                                Point(0, -roofHeight),
                                Point(0, 0),
                                Point(baseUpperWidth/2, roofHeight),
                                Point(baseUpperWidth, 0),
                                Point(baseUpperWidth, -roofHeight),
                            ).toPolygon()
                    }.colorMul(upperBaseBodyColor)
                    shapeView().transform {
                        path = pointArrayListOf(
                            Point(0, 0),
                            Point(baseUpperWidth, 0),
                            Point(baseWidth, metronomeHeight),
                            Point(-baseWidth + baseUpperWidth, metronomeHeight)
                        ).toPolygon()
                    }.colorMul(baseBodyColor)
                }.zIndex(-10),
                camera.container {
                    shapeView().transform {
                        path = pointArrayListOf(
                            Point(-(baseWidth - baseUpperWidth) + outline*2f, 0),
                            Point(bottomBaseWidth, 0),
                            Point(baseWidth, bottomBaseHeight),
                            Point(baseUpperWidth - baseWidth, bottomBaseHeight)
                        ).toPolygon()
                        positionY(metronomeHeight - height)
                    }.colorMul(upperBaseBodyColor)
                }.zIndex(10)
            ).fastForEach {
                it.transform {
                    centerXOn(screen)
                    positionY((screen.height - stickY) * 2 + stickWidth / 2)
                }
            }

            playerStick.body = camera.container().apply body@{
                fastRoundRect(
                    corners = RectCorners(1),
                    size = stickSize, color = playerStickColor
                ) {
                    zIndex = 1f
                    transform {
                        size(stickSize)
                        configureAnchor(this)
                    }
                }
                configurePosition(this)
                shapeView(pointArrayListOf(
                    Point(0, 0),
                    Point(130, 0),
                    Point(100, 130),
                    Point(130 - 100, 130),
                ).map { x, y -> Vector2(x* stickArea, y* stickArea) }.toPointArrayList().toPolygon())
                    .transform {
                    colorMul(playerStickColor)
                    anchor(0.5, 0.5)
                    alignY(this@body, 0.42, true)
                }.apply {
                    addUpdater {
                        if (playerStick.stage.isPausedByUser) return@addUpdater
                        alignY(this@body, 0.42 - min(0.08, playerStick.stage.magnanimity), true)
                    }
                }
            }
        }
    }
}
