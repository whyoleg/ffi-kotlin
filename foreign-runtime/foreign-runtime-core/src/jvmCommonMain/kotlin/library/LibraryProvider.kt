package dev.whyoleg.foreign.library

import dev.whyoleg.foreign.memory.*
import java.util.*

@ForeignMemoryApi
public sealed class LibraryProvider {
    public abstract val libraryName: String

    public abstract class Shared : LibraryProvider()
    public abstract class Embedded : LibraryProvider() {
        public abstract val static: Boolean
        public open fun libraryNameFor(os: LibraryLoader.OS): String = libraryName
    }

    internal companion object {
        val providers by lazy {
            val providersList = ServiceLoader.load(
                LibraryProvider::class.java,
                LibraryProvider::class.java.classLoader
            ).toList()
            val providersMap = providersList.associateBy { it.libraryName }
            check(providersList.size == providersMap.size)
            providersMap
        }
    }
}
