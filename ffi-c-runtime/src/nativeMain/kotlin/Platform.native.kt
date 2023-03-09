package dev.whyoleg.ffi.c

public expect inline fun <reified R : Any> PlatformInt.convert(): R
public expect inline fun <reified R : Any> PlatformUInt.convert(): R
