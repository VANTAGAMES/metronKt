import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.io.net.*
import korlibs.korge.view.*
import korlibs.render.*
import kotlinx.browser.*
import metron.*

suspend fun main() {
    val clientProps = resourcesVfs["client.properties"].readProperties()
    currentUrl = clientProps["server"]!!
    version = clientProps["version"]!!
    redirector = { window.location.replace(it) }
    startMain()
}
