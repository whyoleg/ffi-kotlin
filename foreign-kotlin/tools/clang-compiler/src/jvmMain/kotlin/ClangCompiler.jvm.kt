package dev.whyoleg.foreign.tool.clang.compiler

import dev.whyoleg.foreign.tool.clang.api.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlinx.io.files.Path
import java.nio.file.*
import kotlin.uuid.*

public actual object ClangCompiler {
    private val toolingDirectoryPath by lazy {
        Path(checkNotNull(System.getenv("DEV_WHYOLEG_FOREIGN_TOOLING_DIRECTORY")) {
            "DEV_WHYOLEG_FOREIGN_TOOLING_DIRECTORY environment variable is not set"
        })
    }
    private val temporaryDirectoryPath by lazy {
        System.getenv("DEV_WHYOLEG_FOREIGN_TEMPORARY_DIRECTORY")?.let(::Path) ?: SystemTemporaryDirectory
    }
    private val nativeCliPath by lazy { extractNativeCli() }

    @OptIn(ExperimentalUuidApi::class)
    public actual fun buildIndex(
        headers: Set<String>,
        compilerArgs: List<String>,
        outputPath: String?
    ): CxIndex {
        val requestDirectoryPath = Path(temporaryDirectoryPath, "requests", Uuid.random().toString())
        SystemFileSystem.createDirectories(requestDirectoryPath)
        val inputFilePath = Path(requestDirectoryPath, "input")
        val outputFilePath = outputPath?.let(::Path) ?: Path(requestDirectoryPath, "output")

        encode<ClangCommand>(inputFilePath, ClangCommand.BuildIndex(headers, compilerArgs))

        execute(inputFilePath, outputFilePath)

        return decode(outputFilePath)
    }

    private fun execute(
        inputFilePath: Path,
        outputFilePath: Path
    ) {
        val process = ProcessBuilder().command(
            nativeCliPath.toString(),
            inputFilePath.toString(),
            outputFilePath.toString()
        ).redirectErrorStream(true).start()

        process.inputReader().use {
            while (true) {
                val line: String = it.readLine() ?: break
                println("> $line")
            }
        }
        process.waitFor().also {
            check(it == 0) { "Process failed with error code: $it. Please consult logs above" }
        }
    }

    private fun extractNativeCli(): Path {
        val path = Path(toolingDirectoryPath, "foreign-clang.kexe")
        SystemFileSystem.createDirectories(path.parent!!)
        SystemFileSystem.delete(path, mustExist = false) // we just recreate it
        SystemFileSystem.sink(path).buffered().use { sink ->
            ClangCompiler::class.java.classLoader
                .getResourceAsStream("macosArm64/foreign-tooling-clang.kexe")!!
                .asSource()
                .use(sink::transferFrom)
        }
        Files.setPosixFilePermissions(
            java.nio.file.Path.of(path.toString()),
            setOf(java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE)
        )
        return path
    }
}
