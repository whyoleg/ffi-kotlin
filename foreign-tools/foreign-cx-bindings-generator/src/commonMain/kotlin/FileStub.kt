package dev.whyoleg.foreign.generator.cx

import okio.*

public class FileStub(
    public val path: String,
    public val content: String
) {
    override fun toString(): String = "FileStub($path)"
}

public fun FileSystem.writeFileStub(
    path: Path,
    fileStub: FileStub
) {
    val filePath = path.resolve(fileStub.path)
    createDirectories(filePath.parent!!)
    write(filePath) {
        writeUtf8(fileStub.content)
    }
}

public fun FileSystem.writeFileStubs(path: Path, files: List<FileStub>) {
    deleteRecursively(path)
    files.forEach { writeFileStub(path, it) }
}

internal enum class KotlinImports(
    vararg val imports: String
) {
    Empty,
    Default(
        "dev.whyoleg.foreign.c.*",
        "dev.whyoleg.foreign.platform.*"
    ),
    Enum(
        "kotlin.jvm.*"
    ),
    Struct(
        "dev.whyoleg.foreign.c.*",
        "dev.whyoleg.foreign.memory.*",
        "dev.whyoleg.foreign.memory.access.*",
        "dev.whyoleg.foreign.platform.*",
    ),
    DefaultFFM(
        "import dev.whyoleg.foreign.c.*",
        "import dev.whyoleg.foreign.invoke.*",
        "import dev.whyoleg.foreign.platform.*",
        "import java.lang.foreign.*",
        "import java.lang.invoke.*",
    )
}

internal fun kotlinFile(
    path: String,
    kotlinPackage: String,
    imports: KotlinImports,
    body: StringBuilder.() -> Unit
): FileStub = FileStub(
    path = path,
    content = buildString {
        appendLine("""@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")""").appendLine()
        append("package ").appendLine(kotlinPackage).appendLine()
        if (imports.imports.isNotEmpty()) imports.imports.joinTo(
            this,
            separator = "\n",
            postfix = "\n\n"
        ) { import ->
            "import $import"
        }

        body()
    }
)

internal fun cFile(
    path: String,
    includes: List<String>,
    body: StringBuilder.() -> Unit
): FileStub = FileStub(
    path = path,
    content = buildString {
        includes.joinTo(this, "\n", postfix = "\n\n") { "#include <$it>" }
        body()
    }
)
