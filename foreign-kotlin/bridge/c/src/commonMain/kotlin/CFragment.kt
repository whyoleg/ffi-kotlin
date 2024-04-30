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
