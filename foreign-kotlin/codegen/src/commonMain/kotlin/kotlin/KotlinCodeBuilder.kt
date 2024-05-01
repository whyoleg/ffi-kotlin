package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.codegen.*

internal interface KotlinCodeBuilder : CodeBuilder<KotlinCodeBuilder>

private class KotlinCodeBuilderImpl : KotlinCodeBuilder {
    private val builder = StringBuilder()
    override fun raw(value: String): KotlinCodeBuilder = apply {
        builder.append(value)
    }

    override fun indented(block: KotlinCodeBuilder.() -> Unit): KotlinCodeBuilder = apply {
        KotlinCodeBuilderImpl().apply(block).build().split("\n").joinTo(builder, "\n") {
            if (it.isEmpty()) "" else "    $it"
        }
    }

    fun build(): String = builder.toString()
}

internal fun FilesBuilder.kotlinFile(
    fileName: String,
    packageName: String,
    imports: List<String> = emptyList(),
    block: KotlinCodeBuilder.() -> Unit
) {
    check(fileName.endsWith(".kt")) { "file name must end with .kt" }

    val content = KotlinCodeBuilderImpl().apply {
        raw("package $packageName\n\n")
        if (imports.isNotEmpty()) {
            imports.forEach { raw("import $it\n") }
            raw("\n")
        }
    }.apply(block).build()

    file(fileName, content)
}

internal fun KotlinCodeBuilder.annotation(
    name: String,
    arguments: List<String> = emptyList()
): KotlinCodeBuilder = apply {
    raw("@$name")
    if (arguments.isEmpty()) {
        raw("\n")
    } else if (arguments.size == 1) {
        raw("(${arguments[0]})")
    } else {
        raw("(\n")
        indented {
            arguments.forEach { argument ->
                raw("$argument,\n")
            }
        }
        raw(")\n")
    }
}
