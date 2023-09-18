package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

//
//private fun CxBindingHierarchy.resolve(): Map<String, CxBindingSharedModule> {
//    val sharedModules = mutableMapOf<String, CxBindingSharedModule>()
//
//    fun combine(moduleNames: Set<String>): CxBindingSharedModule = moduleNames.map { moduleName ->
//        sharedModules.getOrPut(moduleName) {
//            modules[moduleName]?.toShared() ?: combine(reversedRelations.getValue(moduleName))
//        }
//    }.combine()
//
//    reversedRelations.forEach { (sharedModuleName, modulesToShare) ->
//        if (sharedModuleName !in sharedModules) {
//            sharedModules[sharedModuleName] = combine(modulesToShare)
//        }
//    }
//
//    return sharedModules
//}

///*internal*/ public fun List<CxBindingSharedModule>.combine(): CxBindingSharedModule {
//    require(isNotEmpty()) { "no modules: critical failure" }
//    if (size == 1) return this[0]
//
//    return CxBindingSharedModule(
//        packages = intersection { it.packages.keys }.associateWith { packageName ->
//            val pkgs = map { it.packages.getValue(packageName) }
//
//            CxBindingsSharedPackage(
//                enums = pkgs.intersection { it.enums.keys }.associateWith { name ->
//                    val enums = pkgs.map { it.enums.getValue(name) }
//
//                    CxBindingSharedEnum(
//                        constantNames = enums.intersection { it.constantNames }
//                    )
//                },
//                records = pkgs.intersection { it.records.keys }.associateWith { name ->
//                    val records = pkgs.map { it.records.getValue(name) }
//
//                    CxBindingSharedRecord(
//                        fieldNames = records.intersection { it.fieldNames }
//                    )
//                }
//            )
//        }
//    )
//}
//
//private fun <T, R> List<T>.intersection(selector: (T) -> Set<R>): Set<R> {
//    require(isNotEmpty()) { "no values: critical failure" }
//    var keys: Set<R>? = null
//    forEach {
//        val value = selector(it)
//        keys = keys?.intersect(value) ?: value
//    }
//    return keys ?: emptySet()
//}
//
///*internal*/ public fun CxBindingModule.toShared(): CxBindingSharedModule = CxBindingSharedModule(
//    packages = packages.mapValues { (_, info) ->
//        CxBindingsSharedPackage(
//            enums = info.enums.mapValues { (_, id) ->
//                val enum = index.enum(id)
//                CxBindingSharedEnum(
//                    constantNames = enum.constants.mapTo(mutableSetOf(), CxEnumInfo.Constant::name)
//                )
//            },
//            records = info.records.mapValues { (_, id) ->
//                val record = index.record(id)
//                CxBindingSharedRecord(
//                    fieldNames = record.members?.fields?.mapTo(mutableSetOf(), CxRecordInfo.Field::name) ?: emptySet()
//                )
//            }
//        )
//    }
//)
//
