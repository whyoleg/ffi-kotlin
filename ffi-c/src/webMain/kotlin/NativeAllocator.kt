package dev.whyoleg.ffi.c

@PublishedApi
internal abstract class NativeAllocator {
    abstract fun allocate(size: Int): NativeMemory

    abstract fun close()

    class Default : NativeAllocator() {
        private val pointers = ArrayDeque<Int>()
        override fun allocate(size: Int): NativeMemory {
            val pointer = FFI.malloc(size)
            pointers.addLast(pointer)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {
            while (true) FFI.free(pointers.removeFirstOrNull() ?: return)
        }
    }

    //TODO
    object Auto : NativeAllocator() {
        override fun allocate(size: Int): NativeMemory {
            val pointer = FFI.malloc(size)
            return NativeMemory(size, NativePointer(pointer))
        }

        override fun close() {

        }
    }
}
