import korlibs.korge.gradle.*

apply<KorgeGradlePlugin>()
apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.korge)
}

korge {
    name = "Metron"
    id = "io.github.bruce0203.${rootProject.name}"
    targetAll()
    targetJvm()
    targetJs()
    targetDesktop()
    //targetDesktopCross()
    targetIos()
    targetAndroid()
    entryPoint = "runMain"
    orientation = Orientation.LANDSCAPE
    icon = File(projectDir, "src/commonMain/resources/images/logo.png"
        .replace("/", File.separator))
//    exeBaseName = ""
//    name = ""
    androidManifestChunks.addAll(setOf(
        """<uses-permission android:name="android.permission.INTERNET" />""",
        """<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />"""
    ))
    serializationJson()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
//            kotlin.addSrcDir(File(project(":shared").projectDir, "src/commonMain/kotlin"))
            dependencies {
                api(project(":deps"))
                api(libs.kotlinx.serialization.protobuf)
                api(libs.kotlinx.uuid)
                api(project(":shared"))
            }
        }
    }
}


fun SourceDirectorySet.addSrcDir(file: File) {
    setSrcDirs(srcDirs.apply { add(file) })
}

@Suppress("UnstableApiUsage")
tasks.withType<ProcessResources> {
    afterEvaluate {
        filesMatching("client.properties") {
            expand(rootProject.properties)
        }
    }
}
