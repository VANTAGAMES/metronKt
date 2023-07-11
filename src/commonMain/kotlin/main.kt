import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 95.0,
    degrees = 60.0,
    magnanimity = 0.5,
    map = (0..1000).map { .5 }
)

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(backgroundColor = Colors[ColorPalette.background]) {
    sceneContainer = sceneContainer()
    resourcesVfs["level.json"].writeString(Json.encodeToString(defaultLevel))
    val level = Json.decodeFromString<Level>(resourcesVfs["level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}
