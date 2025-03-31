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
    folder("tooling") {
        module("cxapi")
        module("cbridge")
        // API and CLI to work with `libclang`
        module("clang")
        // generates code (C, Kotlin, etc)
        module("codegen")
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
