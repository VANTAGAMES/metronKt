import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.image.color.*
import korlibs.io.file.std.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 60.0,
    degrees = 60.0,
    magnanimity = 0.8,
    map = (0..1000).map { .5 }
)

suspend fun main() = Korge(backgroundColor = Colors[ColorPalette.background]) {
    resourcesVfs["level.json"].writeString(Json.encodeToString(defaultLevel))
    val sceneContainer = sceneContainer()
    val level = Json.decodeFromString<Level>(resourcesVfs["level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}
