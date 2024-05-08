package dev.whyoleg.foreign.bridgegen

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.clang.api.*
import dev.whyoleg.foreign.clang.arguments.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.test.*

class BridgegenTest {

    private fun saveFragment(outputPath: String, fragment: CFragment) {
        val indexString = CFragment.encode(fragment)
        val path = Path(outputPath)
        SystemFileSystem.createDirectories(path.parent!!)
        SystemFileSystem.sink(path).buffered().use { it.writeString(indexString) }
    }

    private fun readIndex(inputPath: String): CxIndex {
        val indexString = SystemFileSystem.source(Path(inputPath)).buffered().use { it.readString() }
        return CxIndex.decode(indexString)
    }

    @Test
    fun test() {
        val temp = "/Users/Oleg.Yukhnevich/Projects/whyoleg/ffi-kotlin/foreign-kotlin/build/foreign-temp"
        val map = listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).associate { target ->
            val index = readIndex("$temp/$target/bn/index.json")
            val filteredIndex = readIndex("$temp/$target/bn/index.filtered.json")
            val fragment = generateCFragment(index, "foreign.libcrypto.bn")
            val filteredFragment = generateCFragment(filteredIndex, "foreign.libcrypto.bn")
            saveFragment("$temp/$target/bn/fragment.json", fragment)
            saveFragment("$temp/$target/bn/fragment.filtered.json", filteredFragment)
            target.toString() to fragment
        }

        mergeFragments(map)
    }
}
