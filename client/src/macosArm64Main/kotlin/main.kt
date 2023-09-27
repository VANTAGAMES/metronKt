import korlibs.io.file.std.*
import korlibs.io.lang.*
import kotlinx.coroutines.*
import metron.*

class Main
fun runMain() = main()
fun main() {
    runBlocking {
        val clientProps = resourcesVfs["client.properties"].readProperties()
        currentUrl = clientProps["server"]!!
        version = clientProps["version"]!!
        startMain()
    }
}
