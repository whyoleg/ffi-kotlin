package dev.whyoleg.foreign.cx.playground.main

import dev.whyoleg.foreign.cx.bindings.*
import dev.whyoleg.foreign.cx.index.*
import dev.whyoleg.foreign.cx.playground.*
import okio.Path.Companion.toPath

public fun main() {
    val outputDir = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tools/foreign-cx-playground/build/foreign/typedefs/"

    fun readIndex(target: String): CxIndex = SystemFileSystem.readCxIndex(
        "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tools/foreign-cx-playground/build/foreign/time/$target.json".toPath()
    )

    fun pkg(headerName: String): String {
        return "dev"

        return if (headerName.startsWith("openssl/")) {
            "dev.whyoleg.foreign.bindings.openssl"
        } else {
            "dev.whyoleg.foreign.bindings.openssl.internal"//.${it.substringBefore(".h").replace("/", ".")}"
        }
    }

    fun CxIndex.bPackage(target: String): Map<CxBindingPackageName, CxBPackage> = headers.groupBy {
        CxBindingPackageName(pkg(it.name.value))
    }.mapValues { (name, headers) ->
        CxBPackage(
            name = name,
            typedefs = headers
                .flatMap { it.typedefs }
                .groupBy { it.name }
                .mapValues { mapOf(target to it.value.single()) },
            records = headers
                .flatMap { it.records }
                .filter { it.name != null } //TODO when it could happen? only anonymous?
                .groupBy { it.name!! }
                .mapValues { mapOf(target to it.value.single()) },
            enums = headers
                .flatMap { it.enums }
                .filter { it.name != null } //TODO?
                .groupBy { it.name!! }
                .mapValues { mapOf(target to it.value.single()) },
            functions = headers
                .flatMap { it.functions }
                .groupBy { it.name }
                .mapValues { mapOf(target to it.value.single()) },
        )
    }


    val pkgs = listOf(
        "mingw-x64",
        "linux-x64",
        "macos-x64",
        "macos-arm64",
        "ios-device-arm64",
        "ios-simulator-arm64",
        "ios-simulator-x64",
    ).map { targetName ->
        println("READ: $targetName")
        readIndex(targetName).bPackage(targetName)
    }

    val result = pkgs.flatMap { it.keys }.distinct().map { pkgName ->
        val values = pkgs.mapNotNull { it[pkgName] }
        CxBPackage(
            pkgName,
            typedefs = values.flatMap { it.typedefs.keys }.distinct().associateWith { declarationName ->
                values.mapNotNull { it.typedefs[declarationName] }.reduce(Map<String, CxTypedefInfo>::plus)
            },
            records = values.flatMap { it.records.keys }.distinct().associateWith { declarationName ->
                values.mapNotNull { it.records[declarationName] }.reduce(Map<String, CxRecordInfo>::plus)
            },
            enums = values.flatMap { it.enums.keys }.distinct().associateWith { declarationName ->
                values.mapNotNull { it.enums[declarationName] }.reduce(Map<String, CxEnumInfo>::plus)
            },
            functions = values.flatMap { it.functions.keys }.distinct().associateWith { declarationName ->
                values.mapNotNull { it.functions[declarationName] }.reduce(Map<String, CxFunctionInfo>::plus)
            },
        )
    }

    SystemFileSystem.writePretty(
        "$outputDir/grouped.json".toPath(),
        result
    )
}


internal class TempTest {


    //TODO: DON"T DROP IT
    private fun CxIndex.fix(): CxIndex {
        // remove typedef when it's not needed:
        //   union XXX {};
        //   typedef union XXX XXX;
        fun CxTypedefInfo.canBeSkipped(): Boolean = when (val type = aliased.type) {
            is CxType.Record -> name.value == record(type.id).name?.value
            is CxType.Enum   -> name.value == enum(type.id).name?.value
            else             -> false
        }

        // add name to unnamed union:
        // typedef union {} XXX;
        return CxIndex(headers.map { header ->
            header.copy(
                records = header.records.map { record ->
                    // need to check for blank only - if it's null, it's anonymous, if it's blank - it has name
                    (if (record.name?.value?.isBlank() == true) {
                        headers.firstNotNullOf { otherHeader ->
                            otherHeader.typedefs.firstNotNullOfOrNull { typedef ->
                                when (val type = typedef.aliased.type) {
                                    is CxType.Record -> if (type.id == record.id) {
                                        record.copy(name = typedef.name)
                                    } else {
                                        null
                                    }
                                    else             -> null
                                }
                            }
                        }
                    } else null) ?: record
                },
                enums = header.enums.map { enum ->
                    // need to check for blank only - if it's null, it's anonymous, if it's blank - it has name
                    (if (enum.name?.value?.isBlank() == true) {
                        headers.firstNotNullOf { otherHeader ->
                            otherHeader.typedefs.firstNotNullOfOrNull { typedef ->
                                when (val type = typedef.aliased.type) {
                                    is CxType.Enum -> if (type.id == enum.id) {
                                        enum.copy(name = typedef.name)
                                    } else {
                                        null
                                    }
                                    else           -> null
                                }
                            }
                        }
                    } else null) ?: enum
                }
            )
        }).inlineTypedefs { _, typedef -> typedef.canBeSkipped() }
    }

    //    @Test
    fun typedefs() {
        fun read(arch: String): CxBindingModule {
            //sys._pthread._pthread_types
            val index = SystemFileSystem.readCxIndex(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3-$arch.json".toPath()
            ).fix()
            val packageSelector: (String) -> String = {
                if (it.startsWith("openssl/")) {
                    "dev.whyoleg.foreign.bindings.openssl"
                } else {
                    "dev.whyoleg.foreign.bindings.openssl.internal"//.${it.substringBefore(".h").replace("/", ".")}"
                }
            }

            fun resolveTypedefChain(indent: String, typedef: CxTypedefInfo) {
                println("$indent${typedef.id.value}: ${typedef.name.value} -> ${typedef.aliased.name}")
                when (val type = typedef.aliased.type) {
                    is CxType.Enum    -> {
                        val enum = index.enum(type.id)
                        println("$indent  ENUM: ${enum.id.value}: ${enum.name?.value}")
                    }
                    is CxType.Record  -> {
                        val record = index.record(type.id)
                        println("$indent  RECORD: ${record.id.value}: ${record.name?.value}")
                    }
                    is CxType.Typedef -> {
                        val alias = index.typedef(type.id)
                        resolveTypedefChain("$indent  ", alias)
                    }
                    else              -> println("$indent  CUSTOM: ${typedef.aliased.type}")
                }
            }

//            index.headers.forEach {
//                println("HEADER: ${it.name.value}")
//                it.typedefs.forEach {
//                    resolveTypedefChain("  ", it)
//                }
//            }
//            TODO()

            return CxBindingModule(
                index = index,
                target = CxBindingTarget.Native(CxBindingTarget.Native.KonanTarget.LinuxX64),
                packages = index.headers.groupBy {
                    CxBindingPackageName(packageSelector(it.name.value))
                }.mapValues { (_, headers) ->
                    CxBindingPackageInfo(
                        typedefs = headers
                            .flatMap { it.typedefs }
                            .groupBy { it.name }
                            .mapValues {
                                if (it.value.size > 1) {
                                    it.value.forEach {
                                        resolveTypedefChain("", it)
                                    }
                                    it.value.first().id
                                } else it.value.single().id
                            },
                        records = headers
                            .flatMap { it.records }
                            .filter { it.name != null } //TODO when it could happen? only anonymous?
                            .groupBy { it.name!! }
                            .mapValues { it.value.single().id },
                        enums = headers
                            .flatMap { it.enums }
                            .filter { it.name != null } //TODO?
                            .groupBy { it.name!! }
                            .mapValues { it.value.single().id },
                        emptyMap()
                    )
                }
            )
        }


        val module = read("macos-arm64")
        module.packages.forEach {
            println(it.key.value)

            it.value.typedefs.forEach {
                val typedef = module.index.typedef(it.value)
                println("---   ${typedef.name} -> ${typedef.aliased.name} -> ${typedef.aliased.type}")
            }
        }
//        read("macos-x64")
    }

    //    @Test
    fun runTest() {

//        val fixed = SystemFileSystem.readCxIndex(
//            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3-linux-x64.json".toPath()
//        ).fix()
//
//        SystemFileSystem.writeCxIndex(
//            "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3-linux-x64.fixed".toPath(),
//            fixed
//        )


//        return

        fun process(arch: String): CxBindingSharedModule {
            //sys._pthread._pthread_types
            val index = SystemFileSystem.readCxIndex(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3-$arch.json".toPath()
            ).fix()
            val packageSelector: (String) -> String = {
                if (it.startsWith("openssl/")) {
                    "dev.whyoleg.foreign.bindings.openssl"
                } else {
                    "dev.whyoleg.foreign.bindings.openssl.internal"//.${it.substringBefore(".h").replace("/", ".")}"
                }
            }

            val module = CxBindingModule(
                index = index,
                target = CxBindingTarget.Native(CxBindingTarget.Native.KonanTarget.LinuxX64),
                packages = index.headers.groupBy {
                    CxBindingPackageName(packageSelector(it.name.value))
                }.mapValues { (_, headers) ->
                    CxBindingPackageInfo(
                        typedefs = headers
                            .flatMap { it.typedefs }
                            .groupBy { it.name }
                            .mapValues { it.value.single().id },
                        records = headers
                            .flatMap { it.records }
                            .filter { it.name != null } //TODO when it could happen? only anonymous?
                            .groupBy { it.name!! }
                            .mapValues { it.value.single().id },
                        enums = headers
                            .flatMap { it.enums }
                            .filter { it.name != null } //TODO?
                            .groupBy { it.name!! }
                            .mapValues {
                                if (it.value.size > 1) println(it)
                                it.value.single().id
                            },
                        emptyMap()
                    )
                }
            )

            val shared = module.toShared()

            SystemFileSystem.writePretty(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/packages/libcrypto3-$arch.json".toPath(),
                module.packages
            )

            SystemFileSystem.writePretty(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/shared/libcrypto3-$arch.json".toPath(),
                shared
            )
            return shared
        }


        listOf(
            listOf(
                process("macos-arm64"),
                process("linux-x64")
            ).combine().also {
                SystemFileSystem.writePretty(
                    "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/shared/libcrypto3-nix.json".toPath(),
                    it
                )
            },
            process("mingw-x64")
        ).combine().also {
            SystemFileSystem.writePretty(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/shared/libcrypto3-unix.json".toPath(),
                it
            )
        }

        listOf(
            process("macos-arm64"),
            process("linux-x64"),
            process("mingw-x64")
        ).combine().also {
            SystemFileSystem.writePretty(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/shared/libcrypto3-unix-singleZ.json".toPath(),
                it
            )
        }

        listOf(
            process("macos-arm64"),
            process("ios-device-arm64"),
//            process("mingw-x64")
        ).combine().also {
            SystemFileSystem.writePretty(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/shared/libcrypto3-apple.json".toPath(),
                it
            )
        }

    }
}
