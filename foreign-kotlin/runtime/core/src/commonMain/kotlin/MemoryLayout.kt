package dev.whyoleg.foreign

public sealed class MemoryLayout {
    public abstract val size: MemorySizeInt
    public abstract val alignment: MemorySizeInt

    @Suppress("FunctionName")
    public companion object {
        //
        public fun Void(): MemoryLayout = TODO()
        public fun Byte(): MemoryLayout = TODO()
        public fun Int(): MemoryLayout = TODO()
        public fun Long(): MemoryLayout = TODO()

        public fun Address(targetLayout: MemoryLayout): MemoryLayout = TODO()

        // TODO: leave just Composite or Custom
        public fun Composite(vararg childLayouts: MemoryLayout): MemoryLayout = TODO()
        public fun Custom(size: MemorySizeInt, alignment: MemorySizeInt): MemoryLayout = TODO()

        // or split per struct and union
        public fun Struct(vararg fieldLayouts: MemoryLayout): MemoryLayout = TODO()
        public fun Union(vararg fieldLayouts: MemoryLayout): MemoryLayout = TODO()
    }
}

internal object ByteMemoryLayout : MemoryLayout() {
    override val size: MemorySizeInt get() = Byte.SIZE_BYTES.toMemorySizeInt()
    override val alignment: MemorySizeInt get() = Byte.SIZE_BYTES.toMemorySizeInt()
}
