package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi
public actual class MemorySegment {
    public actual val address: MemoryAddressSize get() = TODO()
    public actual val size: MemoryAddressSize get() = TODO()

    // returns false, when segment is released
    public actual val isAccessible: Boolean get() = TODO()

    public actual fun loadByte(offset: MemoryAddressSize): Byte = TODO()
    public actual fun storeByte(offset: MemoryAddressSize, value: Byte): Unit = TODO()

    public actual fun loadInt(offset: MemoryAddressSize): Int = TODO()
    public actual fun storeInt(offset: MemoryAddressSize, value: Int): Unit = TODO()

    public actual fun loadLong(offset: MemoryAddressSize): Long = TODO()
    public actual fun storeLong(offset: MemoryAddressSize, value: Long): Unit = TODO()

    public actual fun loadPlatformInt(offset: MemoryAddressSize): PlatformInt = TODO()
    public actual fun storePlatformInt(offset: MemoryAddressSize, value: PlatformInt): Unit = TODO()

    public actual fun loadString(offset: MemoryAddressSize): String = TODO()
    public actual fun storeString(offset: MemoryAddressSize, value: String): Unit = TODO()

    public actual fun loadAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout): MemorySegment? = TODO()
    public actual fun storeAddress(offset: MemoryAddressSize, pointedLayout: MemoryLayout, value: MemorySegment?): Unit = TODO()

    public actual fun loadSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout): MemorySegment = TODO()
    public actual fun storeSegment(offset: MemoryAddressSize, valueLayout: MemoryLayout, value: MemorySegment): Unit = TODO()

    public actual companion object {
        public actual val Empty: MemorySegment get() = TODO()
    }
}
