package dev.whyoleg.foreign.tool.cbridge.codegen

import kotlinx.io.*
import kotlinx.io.files.*

internal interface FilesBuilder {
    fun file(path: String, content: String)
    fun directory(path: String, block: FilesBuilder.() -> Unit)
}

internal fun files(rootPath: String, block: FilesBuilder.() -> Unit) {
    val files = mutableSetOf<String>()
    FilesBuilderImpl(rootPath, files).apply(block)
}

private class FilesBuilderImpl(
    private val rootPath: String,
    private val files: MutableSet<String>
) : FilesBuilder {
    override fun file(path: String, content: String) {
        val filePath = "$rootPath/$path"
        if (filePath in files) error("duplicate path: $filePath")
        files.add(filePath)

        SystemFileSystem.sink(
            Path(filePath).also { SystemFileSystem.createDirectories(it.parent!!) }
        ).buffered().use {
            it.writeString(content)
        }
    }

    override fun directory(path: String, block: FilesBuilder.() -> Unit) {
        FilesBuilderImpl("$rootPath/$path", files).block()
    }
}
