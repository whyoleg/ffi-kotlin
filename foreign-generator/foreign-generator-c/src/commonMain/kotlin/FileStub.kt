package dev.whyoleg.foreign.generator.c

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
