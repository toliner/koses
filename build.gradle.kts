group = "dev.toliner"
version = System.getenv("GITHUB_REF")?.takeIf { it.startsWith("refs/tags/") }?.removePrefix("refs/tags/")
    ?: "unspecified"

plugins {
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

if (System.getenv("BINTRAY_API_KEY") != null && System.getenv("BINTRAY_USER") != null) {
    bintray {
        user = System.getenv("BINTRAY_USER")!!
        key = System.getenv("BINTRAY_API_KEY")!!
        publish = true
        pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
            repo = "koses"
            name = "koses"
            version(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.VersionConfig> {
                name = project.version.toString()
            })
        })
    }
}
