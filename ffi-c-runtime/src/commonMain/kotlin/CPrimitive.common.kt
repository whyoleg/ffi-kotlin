package dev.whyoleg.ffi.c

public sealed class CPrimitive<V>(memory: NativeMemory) : CVariable(memory) {
    public abstract var value: V

    public sealed class Type<V, T : CPrimitive<V>> : CVariable.Type<T>()
}

public class CByte internal constructor(memory: NativeMemory) : CPrimitive<Byte>(memory) {
    override val type: Type get() = Type
    override var value: Byte
        get() = memory.loadByte(0)
        set(value) = memory.storeByte(0, value)

    public companion object Type : CPrimitive.Type<Byte, CByte>() {
        override val layout: NativeLayout get() = NativeLayout.Byte
        override fun wrap(memory: NativeMemory): CByte = CByte(memory)
    }
}

public class CUByte internal constructor(memory: NativeMemory) : CPrimitive<UByte>(memory) {
    override val type: Type get() = Type
    override var value: UByte
        get() = memory.loadByte(0).toUByte()
        set(value) = memory.storeByte(0, value.toByte())

    public companion object Type : CPrimitive.Type<UByte, CUByte>() {
        override val layout: NativeLayout get() = NativeLayout.Byte
        override fun wrap(memory: NativeMemory): CUByte = CUByte(memory)
    }
}

public class CInt internal constructor(memory: NativeMemory) : CPrimitive<Int>(memory) {
    override val type: Type get() = Type
    override var value: Int
        get() = memory.loadInt(0)
        set(value) = memory.storeInt(0, value)

    public companion object Type : CPrimitive.Type<Int, CInt>() {
        override val layout: NativeLayout get() = NativeLayout.Int
        override fun wrap(memory: NativeMemory): CInt = CInt(memory)
    }
}

public class CUInt internal constructor(memory: NativeMemory) : CPrimitive<UInt>(memory) {
    override val type: Type get() = Type
    override var value: UInt
        get() = memory.loadInt(0).toUInt()
        set(value) = memory.storeInt(0, value.toInt())

    public companion object Type : CPrimitive.Type<UInt, CUInt>() {
        override val layout: NativeLayout get() = NativeLayout.Int
        override fun wrap(memory: NativeMemory): CUInt = CUInt(memory)
    }
}

public class CLong internal constructor(memory: NativeMemory) : CPrimitive<Long>(memory) {
    override val type: Type get() = Type
    override var value: Long
        get() = memory.loadLong(0)
        set(value) = memory.storeLong(0, value)

    public companion object Type : CPrimitive.Type<Long, CLong>() {
        override val layout: NativeLayout get() = NativeLayout.Long
        override fun wrap(memory: NativeMemory): CLong = CLong(memory)
    }
}

public class CULong internal constructor(memory: NativeMemory) : CPrimitive<ULong>(memory) {
    override val type: Type get() = Type
    override var value: ULong
        get() = memory.loadLong(0).toULong()
        set(value) = memory.storeLong(0, value.toLong())

    public companion object Type : CPrimitive.Type<ULong, CULong>() {
        override val layout: NativeLayout get() = NativeLayout.Long
        override fun wrap(memory: NativeMemory): CULong = CULong(memory)
    }
}
