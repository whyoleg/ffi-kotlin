package dev.whyoleg.foreign.codegen

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.clang.arguments.*
import dev.whyoleg.foreign.codegen.kotlin.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.test.*

class CodegenTest {

    private fun readFragment(inputPath: String): CFragment {
        val indexString = SystemFileSystem.source(Path(inputPath)).buffered().use { it.readString() }
        return CFragment.decode(indexString)
    }

    @Test
    fun test() {
        val temp = "/Users/Oleg.Yukhnevich/Projects/whyoleg/ffi-kotlin/foreign-kotlin/build/foreign-temp"
        val configuration = KotlinCodegenConfiguration(null, false)
        listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).forEach { target ->
            println(target)
            val fragment = readFragment("$temp/$target/bn/fragment.json")
            val filteredFragment = readFragment("$temp/$target/bn/fragment.filtered.json")

//            SystemFileSystem.delete(Path("$temp/$target/bn/files/"), mustExist = false)
            buildFiles("$temp/files/$target/bn/files") {
                directory("common/kotlin") {
                    kotlinFragmentDirectory(configuration, fragment, "common")
                }
            }.forEach { (key, content) ->
                val path = Path(key)
                SystemFileSystem.createDirectories(path.parent!!)
                SystemFileSystem.sink(path).buffered().use { it.writeString(content) }
            }
//            SystemFileSystem.delete(Path("$temp/$target/bn/files.filtered/"), mustExist = false)
            buildFiles("$temp/files/$target/bn/files.filtered") {
                directory("common/kotlin") {
                    kotlinFragmentDirectory(configuration, filteredFragment, "common")
                }
            }.forEach { (key, content) ->
                val path = Path(key)
                SystemFileSystem.createDirectories(path.parent!!)
                SystemFileSystem.sink(path).buffered().use { it.writeString(content) }
            }
        }
    }
}
