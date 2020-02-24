group = "dev.toliner"
version = System.getenv("GITHUB_REF")?.takeIf { it.startsWith("refs/tags/") }?.removePrefix("refs/tags/")
    ?: "unspecified"

plugins {
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}
