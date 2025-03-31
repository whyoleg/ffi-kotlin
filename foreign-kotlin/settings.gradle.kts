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
            from(files("../libs.versions.toml"))
        }
    }
}

projects("foreign-kotlin", prefix = "foreign") {
    folder("runtime") {
        // foreign memory API a.k.a. java.lang.foreign
        module("core")
        // KMP C API
        module("c")
        // uses chasm wasm runtime to execute wasm functions
        // module("chasm")
    }
    folder("tools", prefix = "tool") {
        // API and CLI to work with `libclang`
        module("clang-api") // json serialized API for clang to be transferred between cli and user code
        module("clang-compiler") // uses `libclang` to parse c declaration into `clang-api` models

        // generates code (C, Kotlin, etc)
        module("cbridge-api") // json serialized API to commonize and generate kotlin code
        module("cbridge-commonizer") // takes `clang-api` models and converts them to N `cbridge-api` models
        module("cbridge-codegen") // generates kotlin and c code based on `cbridge-api`, TBD, some metadata may need to be generated
    }

    module("gradle-plugin", "gradle") {
        module("worker")
    }
}

dependencyResolutionManagement {
    repositories {
        // f.e
        // https://download.jetbrains.com/kotlin/native/llvm-11.1.0-linux-x64-essentials.tar.gz
        // foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz
        remoteDistribution(
            url = "https://download.jetbrains.com/kotlin/native/resources/llvm/",
            subgroup = "kotlin-native",
            artifact = "[artifact]/llvm-[artifact]-essentials-[revision].[ext]",
        )

        // f.e
        // https://github.com/whyoleg/openssl-builds/releases/download/openssl-3.0.12_1/openssl-3.0.12.zip
        // foreignbuild.openssl:openssl-3.0.12:3.0.12_1@zip
        remoteDistribution(
            url = "https://github.com/whyoleg/openssl-builds/releases/download",
            subgroup = "openssl",
            artifact = "[revision]/[artifact].[ext]",
        )
    }
}
