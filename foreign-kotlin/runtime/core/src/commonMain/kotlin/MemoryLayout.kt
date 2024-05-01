package dev.whyoleg.foreign

// TODO: make it just a data class with predefined constants for now
public data class MemoryLayout(
    public val size: MemorySizeInt,
    public val alignment: MemorySizeInt
) {
    public companion object {
        private fun primitive(size: MemorySizeInt): MemoryLayout = MemoryLayout(size, size)

        // TODO? Void? Empty? None? Unit?
        public val Void: MemoryLayout = primitive(0.toMemorySizeInt())

        // TODO: C vs Kt types
        public val Byte: MemoryLayout = primitive(kotlin.Byte.SIZE_BYTES.toMemorySizeInt())
        public val Short: MemoryLayout = primitive(kotlin.Short.SIZE_BYTES.toMemorySizeInt())
        public val Int: MemoryLayout = primitive(kotlin.Int.SIZE_BYTES.toMemorySizeInt())
        public val Long: MemoryLayout = primitive(kotlin.Long.SIZE_BYTES.toMemorySizeInt())
        public val PlatformInt: MemoryLayout = primitive(dev.whyoleg.foreign.PlatformInt.SIZE_BYTES.toMemorySizeInt())

        // TODO: is MemorySizeInt fine here?
        public val Address: MemoryLayout = primitive(MemorySizeInt.SIZE_BYTES.toMemorySizeInt())
    }
}
