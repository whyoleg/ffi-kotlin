plugins {
    id("foreignbuild.settings.kotlin-version-catalog")
    id("com.gradle.enterprise")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

gradleEnterprise {
    buildScan {
        publishAlwaysIf(System.getenv("CI").toBoolean())
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        if (System.getenv("GITHUB_REPOSITORY") == "whyoleg/ffi-kotlin") {
            termsOfServiceAgree = "yes"
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//TODO: drop remote distributions
dependencyResolutionManagement {
    repositories {
        // https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-macosx_x64.zip
        // foreignbuild.jdk:macosx_x64:zulu8.72.0.17-ca-jdk8.0.382@zip
        remoteDistribution(
            name = "Zulu JDK",
            url = "https://cdn.azul.com/zulu/bin",
            group = "foreignbuild.jdk",
            artifact = "[revision]-[artifact].[ext]",
        )

        // https://github.com/llvm/llvm-project/releases/download/llvmorg-11.1.0/clang-11.1.0.src.tar.xz
        // foreignbuild.llvm:clang:11.1.0@src.tar.xz
        remoteDistribution(
            name = "LLVM",
            url = "https://github.com/llvm/llvm-project/releases/download",
            group = "foreignbuild.llvm",
            artifact = "llvmorg-[revision]/[artifact]-[revision].[ext]",
        )

        // https://github.com/whyoleg/openssl-builds/releases/download/3.0.8-build-2/openssl3-all.zip
        // foreignbuild.openssl:openssl3-all:3.0.8-build-2@zip
        remoteDistribution(
            name = "Prebuilt OpenSSL",
            url = "https://github.com/whyoleg/openssl-builds/releases/download",
            group = "foreignbuild.openssl",
            artifact = "[revision]/[artifact].[ext]",
        )

        // https://download.jetbrains.com/kotlin/native/llvm-11.1.0-linux-x64-essentials.tar.gz
        // foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz
        remoteDistribution(
            name = "K/N LLVM",
            url = "https://download.jetbrains.com/kotlin/native",
            group = "foreignbuild.kotlin-native",
            artifact = "[artifact].[ext]",
        )
    }
}

fun RepositoryHandler.remoteDistribution(
    name: String,
    url: String,
    group: String,
    artifact: String
) = ivy(url) {
    this.name = "$name distributions"
    metadataSources { artifact() }
    content { includeGroup(group) }
    patternLayout { artifact(artifact) }
}
