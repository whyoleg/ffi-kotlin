package dev.whyoleg.foreign.bridge.c

import kotlinx.serialization.*

@Serializable
public class CFragment(
    public val fragmentType: CFragmentType,
    public val variables: CDeclarations<CVariableData>,
    public val enums: CDeclarations<CEnumData>,
    public val typedefs: CDeclarations<CTypedefData>,
    public val records: CDeclarations<CRecordData>,
    public val functions: CDeclarations<CFunctionData>,
)

// if shared - generate expects
// if not shared - generate actual declarations or just declarations if it's not available in shared
@Serializable
public sealed class CFragmentType {
    @SerialName("shared")
    @Serializable
    public data object Shared : CFragmentType()

    public sealed class Platform : CFragmentType()

    @Serializable
    public sealed class Jvm : Platform() {
        @SerialName("jvm.jni")
        @Serializable
        public data object Jni : Jvm()

        @SerialName("jvm.ffm")
        @Serializable
        public data object Ffm : Jvm()

        @SerialName("jvm.combined")
        @Serializable
        public data object Combined : Jvm()
    }

    @SerialName("js")
    @Serializable
    public data object Js : Platform()

    @SerialName("wasmJs")
    @Serializable
    public data object WasmJs : Platform()

    @SerialName("native")
    @Serializable
    public data object Native : Platform()
}
