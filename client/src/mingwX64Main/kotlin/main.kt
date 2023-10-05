import metron.*
import korlibs.io.net.*
import korlibs.render.*
import korlibs.io.file.std.resourcesVfs
import korlibs.io.lang.readProperties
import kotlinx.coroutines.runBlocking

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
