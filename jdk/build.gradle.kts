plugins {
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.serialization") version Versions.kotlinVersion
    `maven-publish`
    id("com.jfrog.bintray")
}

group = "dev.toliner"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(Dependencies.Serializations.JVM)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    register<Jar>("sourcesJar") {
        group = "build"
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }
}

publishing {
    publications {
        register("koses-jdk", MavenPublication::class) {
            groupId = project.group.toString()
            artifactId = "koses-jdk"
            version = project.version.toString()

            from(project.components["kotlin"]) // java, javaLibraryPlatform, kotlin
            artifact(tasks.getByName("sourcesJar"))
        }
    }
}

if (System.getenv("BINTRAY_API_KEY") != null && System.getenv("BINTRAY_USER") != null) {
    bintray {
        user = System.getenv("BINTRAY_USER")!!
        key = System.getenv("BINTRAY_API_KEY")!!
        setPublications("koses-jdk")
        publish = true
        pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
            repo = "koses"
            name = "koses-jdk"
            version(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.VersionConfig> {
                name = project.version.toString()
            })
        })
    }
}