package dev.whyoleg.foreign.codegen

internal interface FilesBuilder {
    fun file(path: String, content: String)
    fun file(path: String, content: StringBuilder.() -> Unit)
    fun directory(path: String, block: FilesBuilder.() -> Unit)
}
