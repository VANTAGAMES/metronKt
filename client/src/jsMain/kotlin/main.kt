import korlibs.io.file.std.*
import korlibs.io.lang.*
import kotlinx.browser.*
import metron.*

suspend fun main() {
    val clientProps = resourcesVfs["client.properties"].readProperties()
    currentUrl = clientProps["server"]!!
    version = clientProps["version"]!!
    startMain()
    redirector = { document.open(it, "") }
}
