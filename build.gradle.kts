import korlibs.korge.gradle.*
import org.jetbrains.kotlin.utils.addToStdlib.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.korge)
    alias(libs.plugins.kotlinx.serialization)
}

configureAutoVersions()
apply(from = "gradle/repositories.settings.gradle")

allprojects {
    configurations.all {
        resolutionStrategy {
            if (name != "KorgeReloadAgent") eachDependency {
                if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-")) {
                    listOf(
//                        "kotlin-stdlib-common",
                        "kotlin-stdlib-js",
                    ).any { requested.module.name == it }.ifTrue {
//                        useVersion(libs.versions.kotlin.get())
                    }
                }
            }
        }
    }
}
