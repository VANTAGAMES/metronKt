package metron.app.components

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*
import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.geom.shape.*
import korlibs.memory.*
import metron.*
import metron.app.Stage
import metron.util.*
import util.*
import kotlin.math.*

class PlayerStick(val stage: Stage) : Component<PlayerStick> {
    lateinit var body: View
    override fun type() = Companion

    companion object : ComponentHooks<PlayerStick>() {
        val playerStickColor = Colors.WHITESMOKE
        val baseBodyColor = Colors["818589"]
        val upperBaseBodyColor = Colors["D3D3D3"]
        val baseUpperWidth get() = stickHeight/4
        val baseWidth get() = baseUpperWidth*2
        val roofHeight get() = -(stickHeight/20)
        val ratio get() = 1/7f
        val bottomBaseHeight get() = stickHeight*ratio
        val side get() = sqrt(((baseWidth - baseUpperWidth) / 2f).pow(2) + stickHeight.pow(2))
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
                            Point(baseWidth, stickHeight),
                            Point(-baseWidth + baseUpperWidth, stickHeight)
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
                        positionY(stickHeight - height)
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
                    Point(100, 0),
                    Point(70, 100),
                    Point(100 - 70, 100),
                ).toPolygon()).transform {
                    colorMul(playerStickColor)
                    anchor(0.5, 0.5)
                    alignY(this@body, 0.42, true)
                }.apply {
                    addUpdater {
                        alignY(this@body, 0.42 - min(0.08, playerStick.stage.magnanimity), true)
                    }
                }
            }
        }
    }
}
