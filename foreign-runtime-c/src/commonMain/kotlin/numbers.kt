package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import dev.whyoleg.foreign.platform.*
import kotlin.jvm.*
import kotlin.reflect.*

// all those re-declarations are needed to overcome boxing...
@OptIn(ForeignMemoryApi::class)
@get:JvmName("getByteValue")
@set:JvmName("setByteValue")
public inline var CPointer<Byte>.pointed: Byte
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)

@JvmName("getByteValue")
public inline operator fun CPointer<Byte>.getValue(thisRef: Any?, property: KProperty<*>): Byte = pointed

@JvmName("setByteValue")
public inline operator fun CPointer<Byte>.setValue(thisRef: Any?, property: KProperty<*>, value: Byte): Unit = run { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(type: CType.Byte, value: Byte): CPointer<Byte> =
    cPointerOf(type).apply { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(ignored: Byte.Companion, value: Byte = 0): CPointer<Byte> =
    cPointerOf(CType.Byte, value)

public inline fun ForeignCScope.cPointerOf(value: Byte): CPointer<Byte> = cPointerOf(CType.Byte, value)

@JvmName("pointerByte")
public inline fun ForeignCScope.cPointerOf(value: CPointer<Byte>?): CPointer<CPointer<Byte>> =
    cPointerOf(CType.Byte.pointer).apply { pointed = value }

// returned pointer has the same scope as an original pointer
@OptIn(ForeignMemoryApi::class)
public fun CPointer<Byte>.ofUByte(): CPointer<UByte> = CPointer(MemoryAccessor.UByte.at(accessor.offset), segmentInternal2)

@OptIn(ForeignMemoryApi::class)
@get:JvmName("getIntValue")
@set:JvmName("setIntValue")
public inline var CPointer<Int>.pointed: Int
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)

@JvmName("getIntValue")
public inline operator fun CPointer<Int>.getValue(thisRef: Any?, property: KProperty<*>): Int = pointed

@JvmName("setIntValue")
public inline operator fun CPointer<Int>.setValue(thisRef: Any?, property: KProperty<*>, value: Int): Unit = run { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(type: CType.Int, value: Int): CPointer<Int> =
    cPointerOf(type).apply { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(ignored: Int.Companion, value: Int = 0): CPointer<Int> =
    cPointerOf(CType.Int, value)

public inline fun ForeignCScope.cPointerOf(value: Int): CPointer<Int> = cPointerOf(CType.Int, value)

@JvmName("pointerInt")
public inline fun ForeignCScope.cPointerOf(value: CPointer<Int>?): CPointer<CPointer<Int>> =
    cPointerOf(CType.Int.pointer).apply { pointed = value }

@OptIn(ForeignMemoryApi::class)
@get:JvmName("getUIntValue")
@set:JvmName("setUIntValue")
public inline var CPointer<UInt>.pointed: UInt
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)

@JvmName("getUIntValue")
public inline operator fun CPointer<UInt>.getValue(thisRef: Any?, property: KProperty<*>): UInt = pointed

@JvmName("setUIntValue")
public inline operator fun CPointer<UInt>.setValue(thisRef: Any?, property: KProperty<*>, value: UInt): Unit = run { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(type: CType.UInt, value: UInt): CPointer<UInt> =
    cPointerOf(type).apply { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(ignored: UInt.Companion, value: UInt = 0U): CPointer<UInt> =
    cPointerOf(CType.UInt, value)

public inline fun ForeignCScope.cPointerOf(value: UInt): CPointer<UInt> = cPointerOf(CType.UInt, value)

@JvmName("pointerUInt")
public inline fun ForeignCScope.cPointerOf(value: CPointer<UInt>?): CPointer<CPointer<UInt>> =
    cPointerOf(CType.UInt.pointer).apply { pointed = value }

@OptIn(ForeignMemoryApi::class)
@get:JvmName("getPlatformUIntValue")
@set:JvmName("setPlatformUIntValue")
public inline var CPointer<out PlatformUInt>.pointed: PlatformUInt
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)

@JvmName("getPlatformUIntValue")
public inline operator fun CPointer<out PlatformUInt>.getValue(thisRef: Any?, property: KProperty<*>): PlatformUInt = pointed

@JvmName("setPlatformUIntValue")
public inline operator fun CPointer<out PlatformUInt>.setValue(thisRef: Any?, property: KProperty<*>, value: PlatformUInt): Unit =
    run { this.pointed = value }

public inline fun ForeignCScope.cPointerOf(type: CType.PlatformUInt, value: PlatformUInt): CPointer<PlatformUInt> =
    cPointerOf(type).apply { this.pointed = value }

//public inline fun ForeignCScope.allocatePointerFor(ignored: PlatformUInt.Companion, value: PlatformUInt = 0U): CPointer<PlatformUInt> =
//    allocatePointerFor(CType.PlatformUInt, value)

//public inline fun ForeignCScope.allocatePointer(value: PlatformUInt): CPointer<PlatformUInt> = allocatePointerFor(CType.PlatformUInt, value)

//@JvmName("pointerPlatformUInt")
//public inline fun ForeignCScope.allocatePointer(value: CPointer<PlatformUInt>?): CPointer<CPointer<PlatformUInt>> =
//    allocatePointerFor(CType.PlatformUInt.pointer, value)
