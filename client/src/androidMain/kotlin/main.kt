import metron.*
import korlibs.io.file.std.resourcesVfs
import korlibs.io.lang.readProperties
import korlibs.render.*
import korlibs.io.net.*

class Main
suspend fun runMain() {
    main()
}
suspend fun main() {
    val clientProps = resourcesVfs["client.properties"].readProperties()
    currentUrl = clientProps["server"]!!
    version = clientProps["version"]!!
    redirector = { korlibs.korge.view.views().gameWindow.browse(URL(it)) }
    startMain()
}
