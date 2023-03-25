package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

private fun s(pointer: CPointer<Byte>) {
    val v = pointer.value
}


public var <KT : Any> CPointer<KT>.value: KT
    get() = TODO()
    set(value) = TODO()

public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = value
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit =
    run { this.value = value }

@OptIn(ForeignMemoryApi::class)
public var CPointer<Byte>.value: Byte
    get() = TODO()
    set(value) = TODO()

public inline operator fun CPointer<Byte>.getValue(thisRef: Any?, property: KProperty<*>): Byte = value
public inline operator fun CPointer<Byte>.setValue(thisRef: Any?, property: KProperty<*>, value: Byte): Unit = run { this.value = value }

public inline fun MemoryScope.pointerFor(ignored: MemoryLayout.Byte, value: Byte): CPointer<Byte> =
    pointerFor(ignored).apply { this.value = value }

public inline fun MemoryScope.pointerFor(ignored: Byte.Companion, value: Byte = 0): CPointer<Byte> = pointerFor(MemoryLayout.Byte, value)
public inline fun MemoryScope.pointer(value: Byte): CPointer<Byte> = pointerFor(MemoryLayout.Byte, value)

//----

@OptIn(ForeignMemoryApi::class)
public var CPointer<Int>.value: Int
    get() = segment.loadInt(0)
    set(value) = segment.storeInt(0, value)

public inline operator fun CPointer<Int>.getValue(thisRef: Any?, property: KProperty<*>): Int = value
public inline operator fun CPointer<Int>.setValue(thisRef: Any?, property: KProperty<*>, value: Int): Unit = run { this.value = value }

public inline fun MemoryScope.pointer(value: Int = 0): CPointer<Int> = pointerFor(Int).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Int.Companion): CPointer<Int> = pointerFor(CType.Int)
public inline fun MemoryScope.pointer(value: CPointer<Int>?): CPointer<CPointer<Int>> = pointerFor(CType.Int, value)

@OptIn(ForeignMemoryApi::class)
public var CPointer<Long>.value: Long
    get() = segment.loadLong(0)
    set(value) = segment.storeLong(0, value)

public inline operator fun CPointer<Long>.getValue(thisRef: Any?, property: KProperty<*>): Long = value
public inline operator fun CPointer<Long>.setValue(thisRef: Any?, property: KProperty<*>, value: Long): Unit = run { this.value = value }

public inline fun MemoryScope.pointer(value: Long = 0): CPointer<Long> = pointerFor(Long).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Long.Companion): CPointer<Long> = pointerFor(CType.Long)
public inline fun MemoryScope.pointer(value: CPointer<Long>?): CPointer<CPointer<Long>> = pointerFor(CType.Long, value)
