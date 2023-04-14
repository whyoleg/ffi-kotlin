package dev.whyoleg.foreign.memory

import java.nio.*

@ForeignMemoryApi
public object BufferAllocator {
    internal fun allocate(size: MemoryAddressSize): BufferHolder.Root {
        return BufferHolder.Root(ByteBuffer.allocateDirect(size))
    }
}

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
