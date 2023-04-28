package dev.whyoleg.foreign.shared

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.PlatformInt as PInt

@ForeignMemoryApi
internal class SharedMemoryBlockLayout private constructor(
    override val size: MemoryAddressSize,
    override val alignment: MemoryAddressSize
) : MemoryBlockLayout() {
    companion object {
        private fun primitive(size: MemoryAddressSize): MemoryBlockLayout = SharedMemoryBlockLayout(size, size)
        fun void(): MemoryBlockLayout = primitive(memoryAddressSizeZero())
        fun byte(): MemoryBlockLayout = primitive(memoryAddressSize(kotlin.Byte.SIZE_BYTES))
        fun int(): MemoryBlockLayout = primitive(memoryAddressSize(kotlin.Int.SIZE_BYTES))
        fun long(): MemoryBlockLayout = primitive(memoryAddressSize(kotlin.Long.SIZE_BYTES))
        fun address(): MemoryBlockLayout = primitive(memoryAddressSize(PInt.SIZE_BYTES)) //TODO
    }
}

@ForeignMemoryApi
internal class SharedMemoryBlockLayoutRecord(
    private val isUnion: Boolean
) : MemoryBlockLayout.Record() {
    private var _built = false
    private var _size: MemoryAddressSize = memoryAddressSizeZero()
    private var _alignment: MemoryAddressSize = memoryAddressSizeZero()

    override val size: MemoryAddressSize
        get() {
            _built = true
            return _size
        }
    override val alignment: MemoryAddressSize
        get() {
            _built = true
            return _size
        }

    //TODO: recheck
    override fun getOffsetAndAddField(layout: MemoryBlockLayout): MemoryAddressSize {
        check(!_built) { "Layout already built" }

        if (isUnion) {
            if (_size < layout.size) _size = layout.size
            if (_alignment < layout.alignment) _alignment = layout.alignment
            return memoryAddressSizeZero()
        }

        //TODO: recheck which SIZE_BYTES is needed here
        val padding = _size % PInt.SIZE_BYTES
        if (padding != memoryAddressSizeZero() && padding < layout.size) {
            _size += padding
        }

        val offset = _size
        _size += layout.size
        if (_alignment < layout.alignment) {
            _alignment = layout.alignment
        }
        return offset
    }
}
