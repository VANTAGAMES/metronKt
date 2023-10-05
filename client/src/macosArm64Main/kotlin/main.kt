import korlibs.io.file.std.*
import korlibs.io.lang.*
import kotlinx.coroutines.*
import metron.*
import korlibs.render.*
import korlibs.io.net.*

class Main
fun runMain() = main()
fun main() {
    runBlocking {
        val clientProps = resourcesVfs["client.properties"].readProperties()
        currentUrl = clientProps["server"]!!
        version = clientProps["version"]!!
        redirector = { korlibs.korge.view.views().gameWindow.browse(URL(it)) }
        startMain()
    }
}
