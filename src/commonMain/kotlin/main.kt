import korlibs.time.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*

suspend fun main() = Korge(backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
	override suspend fun SContainer.sceneMain() {
		val minDegrees = (-16).degrees
		val maxDegrees = (+16).degrees

        val container = Container()
        containerRoot.addChild(container)

        val stick = solidRect(Size(10, 100))
        container.addChild(stick)

        stick.centerOnStage()
        stick.anchor(0.5f, 1f)

		while (true) {
			stick.tween(stick::rotation[minDegrees], time = 0.25.seconds, easing = Easing.EASE_IN_OUT)
            stick.tween(stick::rotation[maxDegrees], time = 0.25.seconds, easing = Easing.EASE_IN_OUT)
		}
	}
}
