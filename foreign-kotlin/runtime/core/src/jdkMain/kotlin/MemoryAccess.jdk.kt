package dev.whyoleg.foreign

@Suppress("FunctionName")
public actual fun AutoMemoryAccess(): MemoryAccess {
    TODO("Not yet implemented")
}

@Suppress("FunctionName")
public actual fun GlobalMemoryAccess(): MemoryAccess = GlobalMemoryAccess

@Suppress("Since15")
private val GlobalMemoryAccess: MemoryAccess by lazy {
    val foreignAvailable = try {
        // TODO: is it possible to do better?
        // will fail on JVM runtime 8, so we need to do try catch :)
        Runtime.version().feature() >= 22
    } catch (cause: Throwable) {
        false
    }

    if (foreignAvailable) FfmMemoryAccess()
    else TODO()
}
