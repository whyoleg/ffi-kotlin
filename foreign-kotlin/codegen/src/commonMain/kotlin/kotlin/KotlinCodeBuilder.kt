package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.codegen.*

internal interface KotlinCodeBuilder : CodeBuilder<KotlinCodeBuilder>

internal fun FilesBuilder.kotlinFile(
    fileName: String,
    packageName: String,
    imports: List<String> = emptyList(),
    content: KotlinCodeBuilder.() -> Unit
) {
    check(fileName.endsWith(".kt")) { "file name must end with .kt" }

    file(fileName) {
        append("package $packageName\n\n")

        if (imports.isNotEmpty()) imports.joinTo(
            buffer = this,
            prefix = "", separator = "\n", postfix = "\n\n"
        ) { "import $it" }

//        content()
    }
}

internal fun KotlinCodeBuilder.annotation(
    name: String,
    arguments: List<String> = emptyList()
): KotlinCodeBuilder = apply {
    raw("@$name")
    if (arguments.isEmpty()) {
        raw("\n")
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

internal fun KotlinCodeBuilder.emit(
    blocks: MutableList<KotlinCodeBuilder.() -> Unit>.() -> Unit
) {

}
