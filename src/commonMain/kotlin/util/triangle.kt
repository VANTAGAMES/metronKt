package util

import korlibs.image.color.*
import korlibs.image.paint.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.geom.Circle
import korlibs.math.geom.vector.*

inline fun Container.triangle(
    pointList: PointList = pointArrayListOf(Point()),
    fill: Paint = Colors.WHITE,
    stroke: Paint = Colors.WHITE,
    strokeThickness: Float = 0f,
    autoScaling: Boolean = true,
    renderer: GraphicsRenderer = GraphicsRenderer.GPU,
    callback: @ViewDslMarker Triangle.() -> Unit = {}
): Triangle = Triangle(pointList, fill, stroke, strokeThickness, autoScaling, renderer).addTo(this, callback)

open class Triangle(
    private val pointList: PointList = pointArrayListOf(Point()),
    fill: Paint = Colors.WHITE,
    stroke: Paint = Colors.WHITE,
    strokeThickness: Float = 0f,
    autoScaling: Boolean = true,
    renderer: GraphicsRenderer = GraphicsRenderer.GPU,
) : ShapeView(shape = VectorPath(), fill = fill, stroke = stroke, strokeThickness = strokeThickness, autoScaling = autoScaling, renderer = renderer) {
    /** Radius of the circle */
    /** Color of the circle. Internally it uses the [colorMul] property */
    var color: RGBA by ::colorMul

    init {
        updateGraphics()
    }

    private fun updateGraphics() {
        hitShape2d = Polygon(pointList)
        //println("radius=$radius, halfStroke=$halfStroke")
        updatePath {
            clear()
            polyline(pointList, true)
//            assumeConvex = true // Optimization to avoid computing convexity
            //println(toSvgString())
        }
    }
}
