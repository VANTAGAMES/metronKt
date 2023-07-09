import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.image.color.*
import korlibs.io.async.*
import korlibs.io.dynamic.*
import korlibs.io.file.std.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.Json.Default.encodeToString
import org.koin.core.context.*
import org.koin.core.module.dsl.*
import org.koin.dsl.*
import org.koin.mp.KoinPlatform.getKoin

suspend fun main() = Korge(backgroundColor = Colors["#212A3E"]) {
    resourcesVfs["level.json"].writeString(Json.encodeToString(Level(60.0, (0..100).map { .5 })))
    val sceneContainer = sceneContainer()
	sceneContainer.changeTo { Stage() }
}
