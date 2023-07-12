import korlibs.time.*
import korlibs.time.seconds
import kotlinx.serialization.*
import kotlin.time.*

@Serializable
data class Level(
    @SerialName("Bpm")
    val bpm: Double,
    val map: List<Double>,
    val degrees: Double,
    val magnanimity: Double
) {
    val bpmToSec get() = 60.0 / bpm

    val playingTime: TimeSpan get() = bpmToSec.seconds * map.reduce { acc, d -> acc + d }
}
