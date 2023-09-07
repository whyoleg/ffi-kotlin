package dev.whyoleg.foreign.cx.playground

public fun main() {
    listOf(
        "builtin",
        "posix"
    ).forEach { defFile ->
        listOf(
            "macos_arm64",
            "macos_x64",
            "linux_x64",
            "mingw_x64",
            "ios_x64",
            "ios_arm64",
            "ios_simulator_arm64"
        ).forEach { target ->
            println("$defFile/$target")
            ProcessBuilder(
                "/Users/whyoleg/.konan/kotlin-native-prebuilt-macos-aarch64-1.9.0/bin/cinterop",
                "-verbose",
                "-target",
                target,
                "-def",
                "/Users/whyoleg/.konan/kotlin-native-prebuilt-macos-aarch64-1.9.0/konan/platformDef/$target/$defFile.def",
                "-o",
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/temp-cinterop/target"
            ).apply {
                inheritIO()
            }.start().waitFor()
        }
    }

}
