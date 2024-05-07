plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.17.2")
    implementation("com.gradle:common-custom-user-data-gradle-plugin:2.0.1")
    implementation("org.gradle.toolchains:foojay-resolver:0.8.0")
}
