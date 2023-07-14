import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.math.geom.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 90.0,
    degrees = 45.0*2/3,
    offset = -1.7,
    magnanimity = 1.0,
    initialNote = 0.5,
    map = (0..2*60 + 27 / 4).map {
        if (it == 0) .5 else when (it%4) {
            0 -> .5
            1 -> .5
            2 -> .5
            else -> .5
        }
    }
)

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(
    title = "Metron",
    scaleMode = ScaleMode.COVER,
    backgroundColor = Colors[ColorPalette.background]
) {
    sceneContainer = sceneContainer()
    resourcesVfs["levels/level.json"].writeString(Json.encodeToString(defaultLevel))
    val level = Json.decodeFromString<Level>(resourcesVfs["levels/level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}

