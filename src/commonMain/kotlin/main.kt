import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 100.0,
    degrees = 20.0,
    magnanimity =1.0,
    map = (0..1000).map { .5 }
)

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(backgroundColor = Colors[ColorPalette.background]) {
    sceneContainer = sceneContainer()
    resourcesVfs["levels/level.json"].writeString(Json.encodeToString(defaultLevel))
    val level = Json.decodeFromString<Level>(resourcesVfs["levels/level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}
