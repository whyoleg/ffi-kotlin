package dev.whyoleg.foreign.tool.codegen

internal interface CodeBuilder<C : CodeBuilder<C>> {
    fun raw(value: String): C
    fun indented(block: C.() -> Unit): C
}

//internal fun <C : CodeBuilder<C>> C.roundBracketsIndented(block: C.() -> Unit): C {
//    return raw("(").newLine().indented(block).newLine().raw(")")
//}
