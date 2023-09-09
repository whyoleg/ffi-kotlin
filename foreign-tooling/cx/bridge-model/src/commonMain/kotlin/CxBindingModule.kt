package dev.whyoleg.foreign.tooling.cx.bridge.model

// for now: visibility or optIn could be set ONLY for all declarations in an interface

@Serializable
public data class CxBindingModule(
    val index: CxIndex,
    val target: CxBindingTarget.Platform,
    val packages: Map<CxBindingPackageName, CxBindingPackageInfo>
)

/*internal*/ public fun CxBindingModule.toShared(): CxBindingSharedModule = CxBindingSharedModule(
    packages = packages.mapValues { (_, info) ->
        CxBindingsSharedPackage(
            enums = info.enums.mapValues { (_, id) ->
                val enum = index.enum(id)
                CxBindingSharedEnum(
                    constantNames = enum.constants.mapTo(mutableSetOf(), CxEnumInfo.Constant::name)
                )
            },
            records = info.records.mapValues { (_, id) ->
                val record = index.record(id)
                CxBindingSharedRecord(
                    fieldNames = record.members?.fields?.mapTo(mutableSetOf(), CxRecordInfo.Field::name) ?: emptySet()
                )
            }
        )
    }
)

