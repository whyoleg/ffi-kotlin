package dev.whyoleg.foreign

import dev.whyoleg.foreign.memory.*
import java.nio.*

@OptIn(ForeignMemoryApi::class)
internal object ByteBufferMemoryAccess : MemoryAccess() {
    override val implicit: MemoryArena get() = ByteBufferMemoryArena.Implicit
    override fun createArena(): MemoryArena = ByteBufferMemoryArena.Shared()
}

@ForeignMemoryApi
internal object BufferAllocator {
    internal fun allocate(size: MemoryAddressSize): BufferHolder.Root {
        return BufferHolder.Root(ByteBuffer.allocateDirect(size.toInt()))
    }
}

//TODO: inline this into ByteBufferMemoryBlock
internal sealed class BufferHolder(buffer: ByteBuffer) {
    protected var buffer: ByteBuffer? = buffer
    abstract val isAccessible: Boolean
    abstract fun view(view: ByteBuffer): BufferHolder

    class Root(buffer: ByteBuffer) : BufferHolder(buffer), AutoCloseable {
        init {
            check(buffer.isDirect)
            buffer.order(ByteOrder.nativeOrder())
        }

        override val isAccessible: Boolean get() = buffer != null
        override fun view(view: ByteBuffer): BufferHolder = View(view, this)

        override fun close() {
            buffer = null
        }
    }

    private class View(
        buffer: ByteBuffer,
        private val root: Root
    ) : BufferHolder(buffer) {
        override val isAccessible: Boolean get() = root.isAccessible
        override fun view(view: ByteBuffer): BufferHolder = View(view, root)
    }

    fun access(): ByteBuffer {
        if (isAccessible) return buffer!!

        buffer = null
        error("Buffer is not accessible")
    }
}

@ForeignMemoryApi
internal sealed class ByteBufferMemoryArena : MemoryArena() {
    class Shared : ByteBufferMemoryArena() {
        //TODO: cleaner support?
        private val roots = mutableListOf<BufferHolder.Root>()
        private val actions = mutableListOf<() -> Unit>()
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val holder = BufferAllocator.allocate(size)
            roots.add(holder)
            return ByteBufferMemoryBlock(holder)
        }

        override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
            return ByteBufferMemoryBlock.fromAddress(address, layout.size, roots::add)
        }

        override fun close() {
            actions.forEach { it.runCatching { invoke() } }
            actions.clear()
            // no real cleanup for ByteBuffers for now
            roots.forEach { it.close() }
            roots.clear()
        }

        override fun invokeOnClose(block: () -> Unit) {
            actions.add(block)
        }
    }

    object Implicit : ByteBufferMemoryArena() {
        override fun allocate(size: MemoryAddressSize, alignment: MemoryAddressSize): MemoryBlock {
            val holder = BufferAllocator.allocate(size)
            return ByteBufferMemoryBlock(holder)
        }

        override fun wrap(address: MemoryAddressSize, layout: MemoryBlockLayout): MemoryBlock? {
            return ByteBufferMemoryBlock.fromAddress(address, layout.size)
        }

        override fun close() {
            TODO("should not be called on implicit scope")
        }

        override fun invokeOnClose(block: () -> Unit) {
            TODO("should not be called on implicit scope")
        }
    }
}
