package dev.whyoleg.ffi.libcrypto3.static

import dev.whyoleg.ffi.*
import java.nio.file.*
import kotlin.io.path.*

internal class StaticLibcrypto3Loader : LibraryLoader() {
    override fun load() {
        val platform = Platform.current()
        val tempFile = Files.createTempFile("crypto", platform.suffix)
        javaClass.getResourceAsStream(platform.libraryPath("crypto"))!!.use { libraryStream ->
            tempFile.outputStream().use { tempStream ->
                libraryStream.copyTo(tempStream)
            }
        }
        System.load(tempFile.toAbsolutePath().pathString)
    }
}
