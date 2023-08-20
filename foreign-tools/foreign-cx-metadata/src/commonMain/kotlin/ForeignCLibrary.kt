package dev.whyoleg.foreign.cx.metadata

import dev.whyoleg.foreign.cx.index.*
import kotlinx.serialization.*

public fun ForeignCLibrary(
    name: String,
    rootPackage: String,
    index: CxIndex,
    builderAction: ForeignCLibrary.Builder.() -> Unit
): ForeignCLibrary = ForeignCLibrary.Builder().apply(builderAction).build(name, rootPackage, index)

//this is something that should be stored inside klib/jar and accessed by other libraries
// to be able to handle cross-references in dependent libraries
@Serializable
public data class ForeignCLibrary(
    public val name: String,
    public val index: CxIndex,
    public val packages: List<ForeignCPackage> = emptyList()
) {

    public class Builder internal constructor() {
        private var visibilitySelector =
            DeclarationSelector<CxDeclarationInfo, ForeignCDeclaration.Visibility> { _, _ -> ForeignCDeclaration.Visibility.internal }
        private var subpackageSelector =
            DeclarationSelector<CxDeclarationInfo, String> { _, _ -> "" }

        public fun visibility(selector: DeclarationSelector<CxDeclarationInfo, ForeignCDeclaration.Visibility>) {
            visibilitySelector = selector
        }

        public fun subpackage(selector: DeclarationSelector<CxDeclarationInfo, String>) {
            subpackageSelector = selector
        }

        internal fun build(name: String, rootPackage: String, index: CxIndex): ForeignCLibrary {
            class PackageBuilder {
                val typedefs = mutableListOf<ForeignCDeclaration>()
                val records = mutableListOf<ForeignCDeclaration>()
                val enums = mutableListOf<ForeignCDeclaration>()
                val functions = mutableListOf<ForeignCDeclaration>()
            }

            val packages = mutableMapOf<String, PackageBuilder>()
            index.headers.forEach { header ->
                fun add(
                    source: List<CxDeclarationInfo>,
                    destination: PackageBuilder.() -> MutableList<ForeignCDeclaration>
                ) {
                    source.forEach { declaration ->
                        packages.getOrPut(subpackageSelector.select(header, declaration), ::PackageBuilder).destination().add(
                            ForeignCDeclaration(
                                id = declaration.id,
                                visibility = visibilitySelector.select(header, declaration),
                            )
                        )
                    }
                }
                add(header.typedefs, PackageBuilder::typedefs)
                add(header.records, PackageBuilder::records)
                add(header.enums, PackageBuilder::enums)
                add(header.functions, PackageBuilder::functions)
            }

            return ForeignCLibrary(
                name = name,
                index = index,
                packages = packages.map { (name, builder) ->
                    ForeignCPackage(
                        name = when {
                            name.isEmpty()        -> rootPackage
                            rootPackage.isEmpty() -> name
                            else                  -> "$rootPackage.$name"
                        },
                        typedefs = builder.typedefs,
                        records = builder.records,
                        enums = builder.enums,
                        functions = builder.functions
                    )
                }
            )
        }
    }
}

@Serializable
public data class ForeignCPackage(
    public val name: String,
    public val typedefs: List<ForeignCDeclaration> = emptyList(),
    public val records: List<ForeignCDeclaration> = emptyList(),
    public val enums: List<ForeignCDeclaration> = emptyList(),
    public val functions: List<ForeignCDeclaration> = emptyList(),
)

@Serializable
public data class ForeignCDeclaration(
    public val id: CxDeclarationId,
    public val visibility: Visibility,
) {
    @Suppress("EnumEntryName")
    public enum class Visibility { public, internal }
}
