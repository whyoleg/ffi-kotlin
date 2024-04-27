package dev.whyoleg.foreign.codegen

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.clang.arguments.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.test.*

class SimpleTest {

    private fun readFragment(inputPath: String): CFragment {
        val indexString = SystemFileSystem.source(Path(inputPath)).buffered().use { it.readString() }
        return CFragment.decode(indexString)
    }

    @Test
    fun test() {
        val temp = "/Users/Oleg.Yukhnevich/Projects/whyoleg/ffi-kotlin/foreign-kotlin/build/foreign-temp"
        listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).forEach { target ->
            val fragment = readFragment("$temp/$target/bn/fragment.json")
            val filteredFragment = readFragment("$temp/$target/bn/fragment.filtered.json")

            // TODO: generate code
        }
    }
}
