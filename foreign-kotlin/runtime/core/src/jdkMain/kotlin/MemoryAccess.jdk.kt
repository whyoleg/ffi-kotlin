package dev.whyoleg.foreign

@Suppress("FunctionName")
public actual fun AutoMemoryAccess(): MemoryAccess = GlobalMemoryAccess()

@Suppress("FunctionName")
public actual fun GlobalMemoryAccess(): MemoryAccess = when {
    JdkForeignAvailable -> FfmMemoryAccess()
    else                -> TODO()
}
