package dev.whyoleg.foreign.library

import java.util.*

public interface LibraryLoader {
    public val key: String
    public val dependencies: List<String>
    public fun load()
}

//TODO: design better library management,
// so library will be loaded only when it's needed.
// f.e. if there will be multiple foreign libraries attached, now they will be loaded all at once,
// even if only one library is needed on current moment
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
