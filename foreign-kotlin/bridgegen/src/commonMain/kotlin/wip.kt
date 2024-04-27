package dev.whyoleg.foreign.bridgegen

// jvm-macosArm64
// jvm-macosX64
// native-macosArm64
// native-macosX64
// js-wasm
// wasmJs-wasm
// etc...

// group 1 = jvm-macosArm64 * jvm-macosX64 = jvm - single fragment
// group 2 = native-macosArm64 * native-macosX64 = macos + macosArm64-partial + macosX64-partial - 3 fragments

//jvm = merge(jvm1, jvm2)
//macos = commonize(macos1, macos2)
//native = commonize(macos, linux, mingw)
//common = commonize(jvm, native)

//public sealed class CFragmentKind {
//    public abstract val type: CFragmentType
//
//    public data class Platform(
//        override val type: CFragmentType.Platform,
//        val targets: List<ForeignTargetName>
//    ) : CFragmentKind()
//
//    public data class Shared(
//        val fragments: List<CFragmentName>
//    ) : CFragmentKind() {
//        override val type: CFragmentType.Shared get() = CFragmentType.Shared
//    }
//}
//
//private fun s() {
//    val indexs = listOf(
//        "jvm-macosArm64" to "1",
//        "jvm-macosX64" to "2",
//        "native-linuxX64" to "3",
//        "native-macosX64" to "3",
//        "native-macosArm64" to "3",
//    )
//
//    val relations = listOf(
//        "jvm" to platform("jvm-macosArm64", "jvm-macosX64", "jvm-linuxX64"),
//
//        "macosX64" to platform("native-macosX64"),
//        "macosArm64" to platform("native-macosArm64"),
//        "linuxX64" to platform("native-linuxX64"),
//        "mingwX64" to platform("native-mingwX64"),
//
//        "macos" to shared("macosX64", "macosArm64"),
//        "native" to shared("macos", "mingwX64", "linuxX64"),
//        "common" to shared("jvm", "native"),
//    )
//}
//
//public fun buildCBridge(
//    indexes: Map<ForeignTargetName, CxIndex>,
//    fragmentKinds: Map<ForeignFragmentName, ForeignFragmentKind>
//): Map<ForeignFragmentName, CFragment> {
//
//}
//
////public fun merge(
////    indexes: List<CxIndex>
////): CFragment

//public fun parseFragment(
//    name: String,
//    packageName: String,
//    index: CxIndex
//): CFragment {
//    val enums = index.enums.filter { it.name != null }.associateBy { it.id.value }
//    val records = index.records.filter { it.name != null }.associateBy { it.id.value }
//    val typedefs = index.typedefs.associateBy { it.id.value }
//
//    fun CType(type: CxType): CType = when (type) {
//        is CxType.Array       -> CType.Array(CType(type.elementType))
//        is CxType.Enum        -> CType.Enum(enums.getValue(type.id.value).name!!)
//        is CxType.Pointer     -> CType.Pointer(CType(type.pointed))
//        is CxType.Number      -> when (type.value) {
//            CxNumber.Void             -> TODO()
//            CxNumber.Bool             -> TODO()
//            CxNumber.Char             -> TODO()
//            CxNumber.SignedChar       -> TODO()
//            CxNumber.UnsignedChar     -> TODO()
//            CxNumber.Short            -> TODO()
//            CxNumber.UnsignedShort    -> TODO()
//            CxNumber.Int              -> TODO()
//            CxNumber.UnsignedInt      -> TODO()
//            CxNumber.Long             -> TODO()
//            CxNumber.UnsignedLong     -> TODO()
//            CxNumber.LongLong         -> TODO()
//            CxNumber.UnsignedLongLong -> TODO()
//            CxNumber.Int128           -> TODO()
//            CxNumber.UnsignedInt128   -> TODO()
//            CxNumber.Float            -> TODO()
//            CxNumber.Double           -> TODO()
//            CxNumber.LongDouble       -> TODO()
//        }
//
//        is CxType.Typedef     -> CType.Typedef(typedefs.getValue(type.id.value).name)
//        is CxType.Record      -> {
//            val record = records.getValue(type.id.value)
//            if (record.name == null) {
//                CType.Record.Anonymous(
//                    CRecord(
//                        record.isUnion,
//                        record.definition?.fields?.map {
//                            CRecord.Field(
//                                it.name!!,
//                                it.name!!,
//                                CType(it.type)
//                            )
//                        } ?: emptyList()
//                    )
//                )
//            } else {
//                CType.Record.Reference(record.name!!)
//            }
//        }
//
//        is CxType.Function    -> TODO()
//        is CxType.Unsupported -> TODO()
//    }
//
//    fun fileName(fileId: CxFileId): String = when (fileId) {
//        CxFileId.Builtin     -> "builtin"
//        is CxFileId.Included -> fileId.value
//        CxFileId.Root        -> "root"
//    }
//
//    val variables = index.variables.map {
//        CDeclaration(
//            cName = it.name,
//            name = it.name,
//            packageName = packageName,
//            fileName = fileName(it.fileId),
//            availableOn = null,
//            data = CVariable(CType(it.type))
//        )
//    }
//}

//private val testHierarchy = CxBindingHierarchy(
//    modules = mapOf(
//        "jvm" to CxBindingTarget.Jvm(
//            setOf(
//                CxBindingTarget.Jvm.Host.MacosX64,
//                CxBindingTarget.Jvm.Host.MacosArm64,
//            )
//        ),
//        "android" to CxBindingTarget.Android(
//            setOf(
//                CxBindingTarget.Android.Abi.Arm64,
//                CxBindingTarget.Android.Abi.X64,
//            )
//        ),
//        "wasmJs" to CxBindingTarget.WasmJs,
//        "js" to CxBindingTarget.Js,
//        "macosArm64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.MacosArm64
//        ),
//        "macosX64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.MacosX64
//        ),
//        "iosArm64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.IosDeviceArm64
//        ),
//        "iosSimulatorArm64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.IosSimulatorArm64
//        ),
//        "iosX64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.IosSimulatorX64
//        ),
//        "linuxX64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.MacosArm64
//        ),
//        "mingwX64" to CxBindingTarget.Native(
//            CxBindingTarget.Native.KonanTarget.MacosArm64
//        ),
//    ).mapValues {
//        CxBindingModule(
//            CxIndex(emptyList()),
//            it.value,
//            emptyMap()
//        )
//    },
//    relations = mapOf(
//        "jvm" to setOf("jvmCommon", "desktop"),
//        "android" to setOf("jvmCommon"),
//        "jvmCommon" to setOf("common"),
//
//        "macosArm64" to setOf("macos"),
//        "macosX64" to setOf("macos"),
//        "macos" to setOf("apple", "desktop"),
//        "iosArm64" to setOf("ios"),
//        "iosSimulatorArm64" to setOf("ios"),
//        "iosX64" to setOf("ios"),
//        "ios" to setOf("apple"),
//        "linuxX64" to setOf("native", "desktop"),
//        "mingwX64" to setOf("native", "desktop"),
//        "apple" to setOf("native"),
//        "native" to setOf("common"),
//        "desktop" to setOf("common"),
//
//
//        "wasmJs" to setOf("web"),
//        "js" to setOf("web"),
//        "web" to setOf("common"),
//
//        "common" to setOf()
//    )
//)
