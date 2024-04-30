package dev.whyoleg.foreign.codegen

internal interface FilesBuilder {
    fun file(path: String, content: String)
    fun directory(path: String, block: FilesBuilder.() -> Unit)
}

internal fun buildFiles(rootPath: String, block: FilesBuilder.() -> Unit): Map<String, String> {
    val files = mutableMapOf<String, String>()
    FilesBuilderImpl(rootPath, files).apply(block)
    return files.toMap()
}

private class FilesBuilderImpl(
    private val rootPath: String,
    private val files: MutableMap<String, String>
) : FilesBuilder {
    override fun file(path: String, content: String) {
        val filePath = "$rootPath/$path"
        if (files.containsKey(filePath)) error("duplicate path: $filePath")
        files[filePath] = content
    }

    override fun directory(path: String, block: FilesBuilder.() -> Unit) {
        FilesBuilderImpl("$rootPath/$path", files).block()
    }
}
