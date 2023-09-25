package metron.app

import korlibs.datastructure.*
import korlibs.io.file.std.*
import korlibs.time.*
import korlibs.time.seconds
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.time.*

@Serializable
data class Level(
    @SerialName("Name")
    val name: String,
    @SerialName("Author")
    val author: String,
    @SerialName("Bpm")
    val bpm: Double,
    @SerialName("Offset")
    val offset: Double,
    @SerialName("map")
    val map: MutableList<Double> = fastArrayListOf(),
    @SerialName("Degrees")
    val degrees: Double,
    @SerialName("Magnanimity")
    val magnanimity: Double,
    @SerialName("InitialNote")
    val initialNote: Double,
) {
    val bpmToSec get() = 60.0 / bpm

    @Transient
    val playingTime by lazy {
        bpmToSec.seconds * map.reduce { acc, d -> acc + d }
    }
    companion object {
        val default get() = Level(
            name = "Intruder", author = "Riya",
            bpm = 87.5,
            degrees = 45.0*2/3,
            offset = -3.0,
            magnanimity = 1.0,
            initialNote = 0.5,
        ).also {
            (0..((1*60 + 57)*2*(it.bpm/60.0)).toInt()/2).forEach { i ->
                it.map.add(0.5)
                it.map.add(1.5)
            }
        }

        suspend fun loadLevel(levelName: String) =
            Json.decodeFromString<Level>(resourcesVfs[levelName].readString())

    }
}

