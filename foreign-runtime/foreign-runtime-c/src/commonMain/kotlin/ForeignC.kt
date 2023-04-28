package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.jvm.*

@OptIn(ForeignMemoryApi::class)
public inline fun <T> foreignC(block: ForeignCScope.() -> T): T =
    ForeignCScope.arena(MemoryAccess.Auto).use(block)

@ForeignMemoryApi
public inline fun <T> foreignC(memory: MemoryAccess, block: ForeignCScope.() -> T): T =
    ForeignCScope.arena(memory).use(block)

@OptIn(ForeignMemoryApi::class)
@JvmInline
public value class ForeignCScope
private constructor(private val arena: MemoryArena) : AutoCloseable {
    override fun close(): Unit = arena.close()

    //TODO: decide on function names
    public fun cleanup(block: () -> Unit) {
        arena.invokeOnClose(block)
    }

    public inline fun <T> cleanup(resource: T, crossinline block: (T) -> Unit): T {
        cleanup { block(resource) }
        return resource
    }

    public inline fun <T> T.withCleanup(crossinline block: (T) -> Unit): T = cleanup(this, block)

    @ForeignMemoryApi
    @PublishedApi
    internal val unsafe: UnsafeC get() = UnsafeC(arena)

    @ForeignMemoryApi
    public inline fun <T> unsafe(block: UnsafeC.() -> T): T = unsafe.block()

    public companion object {
        public fun implicit(memory: MemoryAccess = MemoryAccess.Auto): ForeignCScope = ForeignCScope(memory.implicit)
        public fun arena(memory: MemoryAccess = MemoryAccess.Auto): ForeignCScope = ForeignCScope(memory.createArena())
    }
}


//for compiler plugin

@Target(AnnotationTarget.CLASS)
public annotation class ForeignCStruct

@Target(AnnotationTarget.CLASS)
public annotation class ForeignCOpaque

@Target(AnnotationTarget.FUNCTION)
public annotation class ForeignCCall

@Target(AnnotationTarget.FUNCTION)
public annotation class ForeignCConst
