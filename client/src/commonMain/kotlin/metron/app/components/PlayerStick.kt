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
        val playerStickColor = Colors["526D82"]
        val baseBodyColor = Colors["818589"]
        val upperBaseBodyColor = Colors["D3D3D3"]
        override val onAdded: ComponentHook<PlayerStick> = { entity, playerStick ->
            val baseUpperWidth = 225f
            val baseWidth = 400f
            val roofHeight = -(stickHeight/20)
            val ratio = 1/6f
            val bottomBaseHeight = stickHeight*ratio
            val side = sqrt(((baseWidth - baseUpperWidth) / 2f).pow(2) + stickHeight.pow(2))
            val outline = sqrt((side * ratio).pow(2) - bottomBaseHeight.pow(2))
            val bottomBaseWidth = baseWidth - outline *2f
            arrayOf(
                screen.container {
                    shapeView(pointArrayListOf(
                        Point(0, -roofHeight),
                        Point(0, 0),
                        Point(baseUpperWidth/2, roofHeight),
                        Point(baseUpperWidth, 0),
                        Point(baseUpperWidth, -roofHeight),
                    ).toPolygon()) {
                    }.colorMul(upperBaseBodyColor)
                    shapeView(pointArrayListOf(
                        Point(0, 0),
                        Point(baseUpperWidth, 0),
                        Point(baseWidth, stickHeight),
                        Point(-baseWidth + baseUpperWidth, stickHeight)
                    ).toPolygon()).colorMul(baseBodyColor)
                }.zIndex(-10),
                screen.container {
                    shapeView(pointArrayListOf(
                        Point(-outline*(7.nextPowerOfTwo), 0),
                        Point(bottomBaseWidth, 0),
                        Point(baseWidth, bottomBaseHeight),
                        Point(baseUpperWidth - baseWidth, bottomBaseHeight)
                    ).toPolygon()) {
                        positionY(stickHeight - height)
                    }.colorMul(upperBaseBodyColor)
                }.zIndex(10)
            ).fastForEach {
                it.centerXOn(screen)
                it.positionY((screen.height - stickY) * 2 + stickWidth / 2)
            }

            playerStick.body = screen.container().apply {
                val stick = fastRoundRect(
                    corners = RectCorners(1),
                    size = stickSize, color = playerStickColor
                ) {
                    zIndex = 1f
//                    Mesh().apply {  }.addTo()
                    transform {
                        size(stickSize)
                        configureAnchor(this)
                    }
                }
                configurePosition(this)
                shapeView(
                    pointArrayListOf(
                        Point(0, 0),
                        Point(100, 0),
                        Point(70, 100),
                        Point(100 - 70, 100),
                    ).toPolygon()
                ) {
                    colorMul(playerStickColor)
                    anchor(0.5, 0.5)
                }.alignY(this, 0.4, true)
            }
        }
    }
}
