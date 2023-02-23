package dev.whyoleg.ffi

public actual typealias COpaque = kotlinx.cinterop.COpaque

public actual abstract class COpaqueType<T : COpaque> : CPointedType<T>()
