import foreignsettings.*

pluginManagement {
    includeBuild("../build-logic")
    includeBuild("../build-settings")
}

plugins {
    id("foreignsettings.default")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

projects("foreign-kotlin", prefix = "foreign") {
    // what is used in runtime
    folder("runtime") {
        module("core")
        module("c")
    }

    // what is required for codegen,
    //  extracted by compiler plugin or indexer
    // mimics `runtime` structure
    folder("bridge") {
        module("c")
    }

    // generates `bridge` models from `clang`(or other) models
    module("bridgegen")

    // ideally, it should generate code which will be processed then by compiler plugin
    // but for now it will generate all code from models
    // generates code (C, Kotlin, etc)
    module("codegen")

    // API and CLI to work with `libclang`
    folder("clang") {
        module("api")
        module("arguments")
        module("compiler")
        module("cli")
        module("cli-commands")
    }

    // processes declarations, modifies code and produce `bridge` models to generate C/JNI/etc via `codegen`
    //module("compiler-plugin")

    module("gradle-plugin")
}

dependencyResolutionManagement {
    repositories {

        // f.e
        // https://github.com/llvm/llvm-project/releases/download/llvmorg-11.1.0/clang-11.1.0.src.tar.xz
        // foreignbuild.llvm:clang:11.1.0@src.tar.xz
        remoteDistribution(
            url = "https://github.com/llvm/llvm-project/releases/download",
            subgroup = "llvm",
            artifact = "llvmorg-[revision]/[artifact]-[revision].[ext]",
        )

        // f.e
        // https://download.jetbrains.com/kotlin/native/llvm-11.1.0-linux-x64-essentials.tar.gz
        // foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz
        remoteDistribution(
            url = "https://download.jetbrains.com/kotlin/native",
            subgroup = "kotlin-native",
            artifact = "[artifact].[ext]",
        )

        // f.e
        // https://github.com/whyoleg/openssl-builds/releases/download/openssl-3.0.12_1/openssl-3.0.12.zip
        // foreignbuild.openssl:openssl-3.0.12:3.0.12_1@zip
        remoteDistribution(
            url = "https://github.com/whyoleg/openssl-builds/releases/download",
            subgroup = "openssl",
            artifact = "[revision]/[artifact].[ext]",
        )

        // f.e
        // https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-macosx_x64.zip
        // foreignbuild.jdk:macosx_x64:zulu8.72.0.17-ca-jdk8.0.382@zip
        remoteDistribution(
            url = "https://cdn.azul.com/zulu/bin",
            subgroup = "jdk",
            artifact = "[revision]-[artifact].[ext]",
        )
    }
}

fun RepositoryHandler.remoteDistribution(url: String, subgroup: String, artifact: String) = ivy(url) {
    name = "$subgroup distributions"
    metadataSources { artifact() }
    content { includeGroup("foreignbuild.$subgroup") }
    patternLayout { artifact(artifact) }
}
