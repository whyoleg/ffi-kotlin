package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

@Suppress("UnusedReceiverParameter")
public inline fun Unsafe.memoryBlock(pointer: CPointer<*>?): MemoryBlock? = pointer?.memoryBlock

@Suppress("UnusedReceiverParameter")
public inline fun Unsafe.memoryBlock(string: CString?): MemoryBlock? = string?.memoryBlock

@Suppress("UnusedReceiverParameter")
public inline fun Unsafe.memoryBlock(array: CArray<*>?): MemoryBlock? = array?.memoryBlock

@Suppress("UnusedReceiverParameter")
public inline fun <KT : Any> Unsafe.cPointer(block: MemoryBlock): CPointer<KT> = CPointer(block)

@Suppress("UnusedReceiverParameter")
public inline fun Unsafe.cString(block: MemoryBlock): CString = CString(block)
