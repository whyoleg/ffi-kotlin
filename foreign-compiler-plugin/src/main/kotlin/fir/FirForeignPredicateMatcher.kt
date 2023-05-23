package dev.whyoleg.foreign.compiler.fir

import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.extensions.predicate.*
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.name.*

val FirSession.foreignPredicateMatcher: FirForeignPredicateMatcher by FirSession.sessionComponentAccessor()

class FirForeignPredicateMatcher(session: FirSession) : FirExtensionSessionComponent(session) {
    private val foreignCCallPredicate = LookupPredicate.create {
        annotated(
            FqName("dev.whyoleg.foreign.c.ForeignCCall")
        )
    }

    val foreignCCalls: Map<CallableId, FirNamedFunctionSymbol> by lazy {
        session.predicateBasedProvider.getSymbolsByPredicate(foreignCCallPredicate)
            .filterIsInstance<FirNamedFunctionSymbol>()
            .associateBy { it.callableId }
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(foreignCCallPredicate)
    }

    fun isForeignCCall(declaration: FirDeclaration): Boolean {
        return declaration is FirSimpleFunction && session.predicateBasedProvider.matches(foreignCCallPredicate, declaration)
    }
}
