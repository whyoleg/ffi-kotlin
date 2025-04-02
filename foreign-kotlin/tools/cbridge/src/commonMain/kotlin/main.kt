package dev.whyoleg.foreign.tool.cbridge

import dev.whyoleg.foreign.tool.cbridge.api.*
import dev.whyoleg.foreign.tool.clang.api.*

public data class CbridgeFragmentRelation(
    val name: CbFragmentName,
    val dependsOn: List<CbFragmentName>,
    val targets: List<CbTarget>,
)

public fun convert(
    indexes: Map<CbTarget, CxIndex>,
    relations: List<CbridgeFragmentRelation>,
    packageName: String
): List<CbFragment> {
    TODO()
}

//private fun test() {
//    convert(
//        emptyMap(),
//        listOf(
//            CbridgeFragmentRelation(
//                name = "commonMain",
//                dependsOn = emptyList(),
//                targets = emptyList()
//            ),
//            CbridgeFragmentRelation(
//                name = "jvmMain",
//                dependsOn = listOf("commonMain"),
//                targets = listOf("jvm-macosArm64", "jvm-mingwX64", "jvm-linuxX64")
//            ),
//            CbridgeFragmentRelation(
//                name = "nativeMain",
//                dependsOn = listOf("commonMain"),
//                targets = emptyList()
//            ),
//            CbridgeFragmentRelation(
//                name = "macosArm64Main",
//                dependsOn = listOf("nativeMain"),
//                targets = listOf("macosArm64")
//            ),
//            CbridgeFragmentRelation(
//                name = "macosX64Main",
//                dependsOn = listOf("nativeMain"),
//                targets = listOf("macosX64")
//            ),
//            CbridgeFragmentRelation(
//                name = "mingwX64Main",
//                dependsOn = listOf("nativeMain"),
//                targets = listOf("mingwX64")
//            )
//        )
//    )
//}
