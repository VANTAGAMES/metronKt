import korlibs.image.color.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.render.SDFShaders.pow
import korlibs.korge.scene.*
import korlibs.math.geom.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

val defaultLevel get() = Level(
    bpm = 87.4275,
    degrees = 45.0*2/3,
    offset = -2.9,
    magnanimity = 1.0,
    initialNote = 0.5,
).also {
    (0..((2*60 + 27)*2*(it.bpm/60.0)).toInt()).forEach { i ->
        it.map.add(0.5)
    }
}

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

