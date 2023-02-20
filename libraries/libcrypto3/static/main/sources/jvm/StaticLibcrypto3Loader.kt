package dev.whyoleg.ffi.libcrypto3.static

import dev.whyoleg.ffi.*
import java.nio.file.*
import kotlin.io.path.*

internal class StaticLibcrypto3Loader : LibraryLoader() {
    override fun load() {
        val platform = Platform.current()
        val tempFile = Files.createTempFile("crypto", platform.suffix)
        val libraryName = when (platform) {
            Platform.WINDOWS_X64 -> "crypto-3-x64"
            else                 -> "crypto"
        }
        javaClass.getResourceAsStream(platform.libraryPath(libraryName))!!.use { libraryStream ->
            tempFile.outputStream().use { tempStream ->
                libraryStream.copyTo(tempStream)
            }
        }
        System.load(tempFile.toAbsolutePath().pathString)
    }
}
