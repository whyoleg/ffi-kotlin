plugins {
    `kotlin-dsl`
    alias(libs.plugins.buildconfig)
}

dependencies {
    implementation(libs.build.gradle.enterprise)
    implementation(libs.build.gradle.foojay)
}

buildConfig {
    packageName("foreignbuild.settings")
    useKotlinOutput {
        topLevelConstants = true
        internalVisibility = true
    }
    buildConfigField("String", "kotlinVersion", libs.versions.kotlin.asProvider().map { "\"$it\"" })
}
