package dev.whyoleg.ffi.c.index

internal object Logger {
    private const val logging = true
    private var indent = 0

    fun log(tag: String, spelling: String?, message: String, prefix: String) {
        if (logging) println("${"-".repeat(indent)}$prefix[$tag | $spelling] $message")
    }

    fun <T> logging(tag: String, spelling: String?, skipLogging: Boolean = false, block: () -> T): T {
        if (!logging || skipLogging) return block()

        log(tag, spelling, "", ">")
        indent++
        val result = runCatching(block)
        indent--
        log(tag, spelling, "$result", "<")
        return result.getOrThrow()
    }
}
