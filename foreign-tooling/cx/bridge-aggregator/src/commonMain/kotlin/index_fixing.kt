package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

// TODO: decide on when to handle this:
//   1. when typedef has the same name as enum/record, f.e.:
//        union XXX {};
//        typedef union XXX XXX;
//   2. when enum/record has no name, but there is declared typedef, f.e.:
//        typedef union {} XXX;

// TODO: DON"T DROP IT

//    private fun CxIndex.fix(): CxIndex {
//        // case 1
//        fun CxTypedefInfo.canBeSkipped(): Boolean = when (val type = aliased.type) {
//            is CxType.Record -> name.value == record(type.id).name?.value
//            is CxType.Enum   -> name.value == enum(type.id).name?.value
//            else             -> false
//        }
//        // case 2  
//        return CxIndex(headers.map { header ->
//            header.copy(
//                records = header.records.map { record ->
//                    // need to check for blank only - if it's null, it's anonymous, if it's blank - it has name
//                    (if (record.name?.value?.isBlank() == true) {
//                        headers.firstNotNullOf { otherHeader ->
//                            otherHeader.typedefs.firstNotNullOfOrNull { typedef ->
//                                when (val type = typedef.aliased.type) {
//                                    is CxType.Record -> if (type.id == record.id) {
//                                        record.copy(name = typedef.name)
//                                    } else {
//                                        null
//                                    }
//                                    else             -> null
//                                }
//                            }
//                        }
//                    } else null) ?: record
//                },
//                enums = header.enums.map { enum ->
//                    // need to check for blank only - if it's null, it's anonymous, if it's blank - it has name
//                    (if (enum.name?.value?.isBlank() == true) {
//                        headers.firstNotNullOf { otherHeader ->
//                            otherHeader.typedefs.firstNotNullOfOrNull { typedef ->
//                                when (val type = typedef.aliased.type) {
//                                    is CxType.Enum -> if (type.id == enum.id) {
//                                        enum.copy(name = typedef.name)
//                                    } else {
//                                        null
//                                    }
//                                    else           -> null
//                                }
//                            }
//                        }
//                    } else null) ?: enum
//                }
//            )
//        }).inlineTypedefs { _, typedef -> typedef.canBeSkipped() }
//    }