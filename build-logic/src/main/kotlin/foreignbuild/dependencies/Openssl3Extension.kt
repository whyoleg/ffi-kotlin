package foreignbuild.dependencies

import org.gradle.api.file.*
import org.gradle.api.provider.*

abstract class Openssl3Extension constructor(
    private val directory: Provider<Directory>
) {
    fun libDirectory(target: String) = directory.map { it.dir("$target/lib") }
    fun includeDirectory(target: String) = directory.map { it.dir("$target/include") }
}
