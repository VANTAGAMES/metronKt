import korlibs.datastructure.*
import korlibs.datastructure.random.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.time.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 85.0,
    degrees = 45.0*2/3,
    magnanimity = 1.0,
    map = (0..10).map { fastArrayListOf(0.5).fastRandom() }
)

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(scaleMode = ScaleMode.COVER, backgroundColor = Colors[ColorPalette.background]) {
    sceneContainer = sceneContainer()
    resourcesVfs["levels/level.json"].writeString(Json.encodeToString(defaultLevel))
    val level = Json.decodeFromString<Level>(resourcesVfs["levels/level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}

