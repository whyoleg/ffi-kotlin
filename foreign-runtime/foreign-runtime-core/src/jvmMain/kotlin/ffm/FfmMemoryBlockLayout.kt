package dev.whyoleg.foreign.ffm

import dev.whyoleg.foreign.memory.*
import java.lang.foreign.*

@ForeignMemoryApi
internal class FfmMemoryBlockLayout(
    val layout: MemoryLayout
) : MemoryBlockLayout() {
    override val size: MemoryAddressSize get() = layout.byteSize()
    override val alignment: MemoryAddressSize get() = layout.byteAlignment()
}

@ForeignMemoryApi
internal class FfmMemoryBlockLayoutRecord(
    private val isUnion: Boolean
) : MemoryBlockLayout.Record() {
    private var fields: MutableList<MemoryLayout>? = mutableListOf()
    private var _size: Long = 0
    val layout: MemoryLayout by lazy {
        val fields = checkNotNull(fields)
        val layout = when (isUnion) {
            true  -> MemoryLayout.unionLayout(*(fields.toTypedArray()))
            false -> MemoryLayout.structLayout(*(fields.toTypedArray()))
        }
        this.fields = null

        layout
    }
    override val size: MemoryAddressSize get() = layout.byteSize()
    override val alignment: MemoryAddressSize get() = layout.byteAlignment()

    override fun getOffsetAndAddField(layout: MemoryBlockLayout): MemoryAddressSize {
        val fields = checkNotNull(fields) { "Layout already built" }
        fields += layout.ffmMemoryLayout()

        if (isUnion) return memoryAddressSizeZero()

        //TODO: recheck which SIZE_BYTES is needed here
        val padding = _size % kotlin.Long.SIZE_BYTES
        if (padding != memoryAddressSizeZero() && padding < layout.size) {
            _size += padding
            fields += MemoryLayout.paddingLayout(padding * 8)
        }

        val offset = _size
        _size += layout.size
        return offset
    }
}
