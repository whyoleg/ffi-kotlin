package dev.whyoleg.foreign.memory

@ForeignMemoryApi
public abstract class MemoryBlockLayout internal constructor() {
    public abstract val alignment: MemoryAddressSize
    public abstract val size: MemoryAddressSize

    public companion object {
        public val Void: MemoryBlockLayout = createVoidMemoryBlockLayout()
        public val Byte: MemoryBlockLayout = createByteMemoryBlockLayout()
        public val Int: MemoryBlockLayout = createIntMemoryBlockLayout()
        public val Long: MemoryBlockLayout = createLongMemoryBlockLayout()
        public val PlatformInt: MemoryBlockLayout = createPlatformIntMemoryBlockLayout()
        public val Address: MemoryBlockLayout = createAddressMemoryBlockLayout()
        public fun Record(isUnion: Boolean): Record = createRecordMemoryBlockLayout(isUnion)
    }

    public abstract class Record internal constructor() : MemoryBlockLayout() {
        public abstract fun getOffsetAndAddField(layout: MemoryBlockLayout): MemoryAddressSize
    }
}
