import korlibs.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}

korge {
	id = "com.sample.demo"

// To enable all targets at once

	targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

	targetJvm()
	targetJs()
	targetDesktop()
	targetIos()
	targetAndroid()

	serializationJson()
}


dependencies {
    add("commonMainApi", project(":deps"))
    //add("commonMainApi", project(":korge-dragonbones"))
}

@Suppress("UnstableApiUsage")
tasks.withType<ProcessResources> {
    filesMatching("client.properties") {
        println(properties["version"])
        expand("version" to properties["version"])
    }
}

