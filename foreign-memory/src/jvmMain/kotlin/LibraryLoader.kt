package dev.whyoleg.foreign.memory

import java.util.*

public interface LibraryLoader {
    public val key: String
    public val dependencies: List<String>
    public fun load()
}

internal fun loadLibraries() {
    val loaders = ServiceLoader.load(
        LibraryLoader::class.java,
        LibraryLoader::class.java.classLoader
    ).iterator().asSequence().toList()

    val loaded = mutableSetOf<String>()
    val loadersByKeys = loaders.associateBy { it.key }
    check(loaders.size == loadersByKeys.size) { "Duplicated library name" }

    fun LibraryLoader.loadAll() {
        dependencies.forEach { dependency ->
            if (dependency in loaded) return@forEach
            loadersByKeys.getValue(dependency).loadAll()
        }
        if (key in loaded) return
        load()
        loaded += key
    }
    loaders.forEach(LibraryLoader::loadAll)

    check(loaders.size == loaded.size) { "Not all libraries are loaded" }
}
