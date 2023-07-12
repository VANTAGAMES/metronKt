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
    bpm = 175.0/2,
    degrees = 45.0*2/3,
    offset = 1.0,
    magnanimity = 1.0,
    map = (0..100).map {
        if (it == 0) .5 else when (it%4) {
            0 -> .5
            1 -> .5
            2 -> .5
            else -> .5
        }
    }
)

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(scaleMode = ScaleMode.COVER, backgroundColor = Colors[ColorPalette.background]) {
    sceneContainer = sceneContainer()
    resourcesVfs["levels/level.json"].writeString(Json.encodeToString(defaultLevel))
    val level = Json.decodeFromString<Level>(resourcesVfs["levels/level.json"].readString())
	sceneContainer.changeTo { Stage(level) }
}

