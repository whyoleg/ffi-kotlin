package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CByteVariable(segment: MemorySegment) : CVariable(segment)

public actual object CByteVariableType : CVariableType<CByteVariable>(::CByteVariable, ValueLayout.JAVA_BYTE)

public actual var CByteVariable.value: CByte
    get() = segment.get(ValueLayout.JAVA_BYTE, 0)
    set(value) = segment.set(ValueLayout.JAVA_BYTE, 0, value)

public actual fun CPointer<CByteVariable>.toUByte(): CPointer<CUByteVariable> = CPointer(CUByteVariable(pointed.segment))

public actual fun CPointer<CUByteVariable>.toByte(): CPointer<CByteVariable> = CPointer(CByteVariable(pointed.segment))
