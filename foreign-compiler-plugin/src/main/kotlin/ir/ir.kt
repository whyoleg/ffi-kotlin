package dev.whyoleg.foreign.compiler.ir

import dev.whyoleg.foreign.compiler.fir.*
import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.ir.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.visitors.*

class ForeignIrGenerationExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        println("IR: ${pluginContext.platform}")
        moduleFragment.acceptChildrenVoid(ForeignCCallIrTransformer(pluginContext))
    }
}

class ForeignCCallIrTransformer(
    private val context: IrPluginContext,
) : IrElementVisitorVoid {
    private val irFactory = context.irFactory
    private val irBuiltIns = context.irBuiltIns

    override fun visitElement(element: IrElement) {
        when (element) {
            is IrFile,
            is IrModuleFragment -> element.acceptChildrenVoid(this)
            else                -> {}
        }
    }

    override fun visitSimpleFunction(declaration: IrSimpleFunction) {
        val origin = declaration.origin
        println("${declaration.symbol}: $origin")
        if (origin !is IrDeclarationOrigin.GeneratedByPlugin || origin.pluginKey != FirForeignCCallGenerator.Key) return
        require(declaration.body == null)
        declaration.body = generateBodyForFunction(declaration)
        super.visitSimpleFunction(declaration)
    }

    private fun generateBodyForFunction(function: IrSimpleFunction): IrBody {
        val const = IrConstImpl(
            -1, -1,
            irBuiltIns.stringType,
            IrConstKind.String,
            context.platform.toString()
        )
        val returnExpression = IrReturnImpl(-1, -1, irBuiltIns.stringType, function.symbol, const)
        return irFactory.createBlockBody(-1, -1, listOf(returnExpression))
    }
}
