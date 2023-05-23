package dev.whyoleg.foreign.compiler.fir

import org.jetbrains.kotlin.*
import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.*
import org.jetbrains.kotlin.fir.extensions.*
import org.jetbrains.kotlin.fir.plugin.*
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.name.*

class FirForeignCCallStatusTransformer(session: FirSession) : FirStatusTransformerExtension(session) {
    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        return session.foreignPredicateMatcher.isForeignCCall(declaration)
    }

    override fun transformStatus(status: FirDeclarationStatus, declaration: FirDeclaration): FirDeclarationStatus {
        return status.copy(
            isExpect = true
        )
    }
}

class FirForeignCCallGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {
    override fun generateFunctions(callableId: CallableId, context: MemberGenerationContext?): List<FirNamedFunctionSymbol> {
        val childSession = session.moduleData.dependsOnDependencies.firstOrNull()?.session ?: return emptyList()
        val original = childSession.foreignPredicateMatcher.foreignCCalls.getValue(callableId)
        return listOf(
            createTopLevelFunction(Key, callableId, original.resolvedReturnType) {
                visibility = original.visibility
                status {
                    isActual = true
                }
                original.valueParameterSymbols.forEach {
                    valueParameter(
                        it.name,
                        it.resolvedReturnType,
                    )
                }
            }.symbol
        )
    }

    override fun getTopLevelCallableIds(): Set<CallableId> {
        val childSession = session.moduleData.dependsOnDependencies.firstOrNull()?.session ?: return emptySet()
        return childSession.foreignPredicateMatcher.foreignCCalls.keys
    }

    object Key : GeneratedDeclarationKey()
}
