package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

@Suppress("UnusedReceiverParameter")
public inline fun Unsafe.memoryBlock(pointer: CPointer<*>): MemoryBlock = pointer.memoryBlock

@Suppress("UnusedReceiverParameter")
public inline fun <KT : Any> Unsafe.allocateCPointer(block: MemoryBlock): CPointer<KT> = CPointer(block)
