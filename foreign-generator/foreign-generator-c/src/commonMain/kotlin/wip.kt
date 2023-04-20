package dev.whyoleg.foreign.generator.c

import dev.whyoleg.foreign.cx.index.*

internal const val INDENT = "    "
internal const val PREFIX = "foreign_"

internal val CxDeclarationInfo.prefixedName get() = "$PREFIX${name.value}"

@Suppress("EnumEntryName")
internal enum class Visibility {
    public, internal
}

public sealed class Target {
    public sealed class Emscripten : Target() {
        public object JS : Emscripten()
        public object WASM : Emscripten()
    }

    public sealed class JVM : Target() {
        public object JNI : JVM()
        public object FFM : JVM()
    }

    public object Native : Target()
}

