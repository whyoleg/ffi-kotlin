package dev.whyoleg.foreign.tooling.cx.compiler.model

import kotlinx.serialization.*

// todo: redesign declarations - no need to have maps?
@Serializable
public data class CxCompilerIndex(
    val variables: CxCompilerDeclarations<CxCompilerVariableData>,
    val enums: CxCompilerDeclarations<CxCompilerEnumData>,
    val records: CxCompilerDeclarations<CxCompilerRecordData?>,
    val typedefs: CxCompilerDeclarations<CxCompilerTypedefData>,
    val functions: CxCompilerDeclarations<CxCompilerFunctionData>,
)
