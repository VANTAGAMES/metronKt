import kotlinx.serialization.*

@Serializable
data class Level(
    @SerialName("Bpm")
    val bpm: Double,
    val map: List<Double>
)

