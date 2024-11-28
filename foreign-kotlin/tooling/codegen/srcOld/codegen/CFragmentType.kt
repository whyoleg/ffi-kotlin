package dev.whyoleg.foreign.codegen

public sealed class CFragmentType {
    public data object Shared : CFragmentType()

    public sealed class Platform : CFragmentType()

    public sealed class Jvm : Platform() {
        public data object Jni : Jvm()
        public data object Ffm : Jvm()
        public data object Combined : Jvm()
    }

    public data object Js : Platform()
    public data object WasmJs : Platform()
    public data object Native : Platform()
}
