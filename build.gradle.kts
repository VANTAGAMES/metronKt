import korlibs.korge.gradle.*

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.korge)
}

configureAutoVersions()
apply(from = "gradle/repositories.settings.gradle")

