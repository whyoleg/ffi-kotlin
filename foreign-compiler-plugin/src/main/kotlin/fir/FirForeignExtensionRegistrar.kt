package dev.whyoleg.foreign.compiler.fir

import org.jetbrains.kotlin.fir.extensions.*

class FirForeignExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::FirForeignCCallStatusTransformer
        +::FirForeignPredicateMatcher
        +::FirForeignCCallGenerator
    }
}
