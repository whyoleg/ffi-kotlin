package dev.whyoleg.foreign

internal sealed class Cleaner {
    abstract fun clean()
}

internal abstract class CleanupAction {
    abstract fun run()
}

internal fun createCleaner(action: CleanupAction): Cleaner {
    val cleaner = CleanerImpl(action)
    registry.register(cleaner, action)
    return cleaner
}

private external class FinalizationRegistry(cleanup: (CleanupAction) -> Unit) {
    fun register(cleaner: Cleaner, action: CleanupAction)
    fun unregister(cleaner: Cleaner)
}

private val registry = FinalizationRegistry(CleanupAction::run)

private class CleanerImpl(
    private val action: CleanupAction
) : Cleaner() {
    private var executed = false
    override fun clean() {
        if (executed) return
        executed = true
        registry.unregister(this)
        action.run()
    }
}
