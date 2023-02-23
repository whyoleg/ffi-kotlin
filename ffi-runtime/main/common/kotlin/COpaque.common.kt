package dev.whyoleg.ffi

public expect abstract class COpaque : CPointed
public expect abstract class COpaqueType<T : COpaque> : CPointedType<T>
