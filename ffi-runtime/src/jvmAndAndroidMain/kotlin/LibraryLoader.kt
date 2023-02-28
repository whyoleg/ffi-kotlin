package dev.whyoleg.ffi

import java.util.*

//TODO: establish depends on
public interface LibraryLoader {
    public fun load()
}

internal fun loadLibraries() {
    ServiceLoader.load(
        LibraryLoader::class.java,
        LibraryLoader::class.java.classLoader
    ).iterator().asSequence().toList().forEach(LibraryLoader::load)
}
