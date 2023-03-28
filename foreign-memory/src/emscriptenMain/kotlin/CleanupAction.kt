package dev.whyoleg.foreign.memory

internal fun interface CleanupAction {
    fun clean()
}

internal class Cleaner {
    fun unregister() {
        registry.unregister(this)
    }
}

internal fun createCleaner(action: CleanupAction): Cleaner {
    val cleaner = Cleaner()
    registry.register(cleaner, action)
    return cleaner
}

private external class FinalizationRegistry(cleanup: (CleanupAction) -> Unit) {
    fun register(cleaner: Cleaner, action: CleanupAction)
    fun unregister(cleaner: Cleaner)
}

private val registry = FinalizationRegistry(CleanupAction::clean)
