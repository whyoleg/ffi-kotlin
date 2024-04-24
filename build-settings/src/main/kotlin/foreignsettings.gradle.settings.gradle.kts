plugins {
    id("com.gradle.develocity")
    id("com.gradle.common-custom-user-data-gradle-plugin")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

develocity {
    buildScan.publishing.onlyIf { System.getenv("CI").toBoolean() }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
