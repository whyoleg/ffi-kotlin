package dev.whyoleg.foreign.codegen.kotlin

import dev.whyoleg.foreign.bridge.c.*
import dev.whyoleg.foreign.codegen.*

internal fun KotlinCodeBuilder.expectRecord(
    configuration: KotlinCodegenConfiguration,
    index: CFragmentIndex,
    record: CRecord
) {
    val name = record.description.ktName
    val visibility = configuration.visibility.value

    when (val definition = record.definition) {
        null -> {
            raw("$visibility expect class $name: COpaque {\n")
            indented {
                raw("$visibility companion object: CType<$name>\n")
            }
            raw("}\n")
        }

        else -> {
            raw("@JvmInline\n")
            raw("$visibility expect value class $name\n")
            raw("@PublishedApi internal constructor(\n")
            indented {
                // TODO: memoryBlock clash with field names
                raw("@PublishedApi internal val memoryBlock: MemoryBlock\n")
            }
            raw(") : CStruct {\n")
            indented {
                raw("$visibility companion object: CType<$name>\n\n")

                // TODO: support anonymousRecords
                if (definition.anonymousRecords.isEmpty()) {
                    definition.fields.forEach { field ->
                        raw("$visibility var ${field.ktName}: ${field.type.asKotlinTypeString(index)}\n")
                    }
                }
            }
            raw("}\n")
        }
    }
}

internal fun KotlinCodeBuilder.actualRecord(
    configuration: KotlinCodegenConfiguration,
    index: CFragmentIndex,
    record: CRecord
) {
    val name = record.description.ktName
    val visibility = configuration.visibility.value
    val actuality = "actual "

    when (val definition = record.definition) {
        null -> {
            raw("$visibility $actuality class $name private constructor(): COpaque {\n")
            indented {
                raw("override fun Unsafe.memoryLayout(): MemoryLayout = MemoryLayout.Void()\n")
                raw("$visibility $actuality companion object: CType<$name> {\n")
                indented {
                    raw("override fun Unsafe.memoryLayout(): MemoryLayout = MemoryLayout.Void()\n")
                }
                raw("}\n")
            }
            raw("}\n")
        }

        else -> {
            raw("@JvmInline\n")
            raw("$visibility $actuality value class $name\n")
            raw("@PublishedApi internal constructor(\n")
            indented {
                // TODO: memoryBlock clash with field names
                raw("@PublishedApi internal val memoryBlock: MemoryBlock\n")
            }
            raw(") : CStruct {\n")
            indented {
                // TODO: support anonymousRecords
                if (definition.anonymousRecords.isEmpty()) {
                    definition.fields.forEach { field ->
                        // TODO: getter clash with `value`?
                        val fieldTypeName = field.ktName
                        val fieldTypeString = field.type.asKotlinTypeString(index)
                        val fieldTypeOffset = "Companion.${fieldTypeName}_offset"

                        when (val resolvedType = field.type.resolvedType(index)) {
                            is CType.Typedef     -> TODO("should not happen")

                            is CType.Record      -> {
                                raw("$visibility inline val $fieldTypeName: $fieldTypeString\n")
                                indented {
                                    raw("get() = $fieldTypeString(memoryBlock.slice($fieldTypeOffset, Unsafe.memoryLayout($fieldTypeString)))\n")
                                }
                            }

                            is CType.Pointer     -> {
                                raw("$visibility inline var $fieldTypeName: $fieldTypeString\n")
                                indented {
                                    raw("get() = getPointed($fieldTypeOffset, MemoryLayout.TODO(${resolvedType.pointed}))\n")
                                    raw("set(value) = setPointed($fieldTypeOffset, Unsafe.memoryBlock(value))\n")
                                }
                            }

                            is CType.Array       -> TODO()
                            CType.Boolean        -> TODO()
                            is CType.Enum        -> TODO()
                            is CType.Function    -> TODO()
                            is CType.Mixed       -> TODO()
                            CType.Void           -> TODO()
                            is CType.Unsupported -> TODO()
                            is CType.Number      -> TODO()
                            else                 -> {
                                val accessor = "XXX"
                                raw("$visibility inline var $fieldTypeName: $fieldTypeString\n")
                                indented {
                                    raw("get() = get$accessor($fieldTypeOffset)\n")
                                    raw("set(value) = set$accessor($fieldTypeOffset, value)\n")
                                }
                            }
                        }
                    }
                }

                raw("override fun Unsafe.memoryLayout(): MemoryLayout = Unsafe.memoryLayout(Companion)\n")
                raw("$visibility $actuality companion object : CType<$name> {\n")
                indented {
                    definition.fields.forEach { field ->
                        raw("private const val ${field.ktName}_offset = ${field.offset}\n")
                    }
                    raw("private val memoryLayout = MemoryLayout.of(${definition.size}, ${definition.align})\n")
                    raw("override fun Unsafe.memoryLayout(): MemoryLayout = memoryLayout\n")
                }
                raw("}\n")
            }

            raw("}\n")
        }
    }
}
