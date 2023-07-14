import korlibs.datastructure.*
import korlibs.time.*
import korlibs.time.seconds
import kotlinx.serialization.*
import kotlin.time.*

@Serializable
data class Level(
    @SerialName("Bpm")
    val bpm: Double,
    val offset: Double,
    val map: MutableList<Double> = fastArrayListOf(),
    val degrees: Double,
    val magnanimity: Double,
    val initialNote: Double,
) {
    val bpmToSec get() = 60.0 / bpm

    @Transient
    val playingTime by lazy {
        bpmToSec.seconds * map.reduce { acc, d -> acc + d }
    }
}
