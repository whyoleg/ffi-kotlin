package dev.whyoleg.foreign.bridge.c

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public class CFragment(
    // public val fragmentType: CFragmentType,
    public val variables: List<CVariable>,
    public val enums: List<CEnum>,
    public val typedefs: List<CTypedef>,
    public val records: List<CRecord>,
    public val functions: List<CFunction>,
) {

    public companion object {
        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }

        public fun encode(fragment: CFragment): String = json.encodeToString(serializer(), fragment)
        public fun decode(string: String): CFragment = json.decodeFromString(serializer(), string)
    }
}

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
