plugins {
    id("com.gradle.enterprise") version "3.12.2"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

rootProject.name = "ffi-kotlin"

includeBuild("foreign-runtime")
includeBuild("foreign-tooling")
includeBuild("foreign-gradle-plugin")

