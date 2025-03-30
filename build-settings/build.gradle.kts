plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.19.2")
    implementation("com.gradle:common-custom-user-data-gradle-plugin:2.2.1")
    implementation("org.gradle.toolchains:foojay-resolver:0.9.0")
}
