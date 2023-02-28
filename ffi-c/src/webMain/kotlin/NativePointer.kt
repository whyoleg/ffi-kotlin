package dev.whyoleg.ffi

public class NativePointer(
    public val value: Int,
) {
    public companion object {
        public val NULL: NativePointer = NativePointer(0)
    }
}
