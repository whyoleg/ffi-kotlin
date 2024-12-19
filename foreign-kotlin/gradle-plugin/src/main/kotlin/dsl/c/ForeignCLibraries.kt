package dev.whyoleg.foreign.gradle.dsl.c

import org.gradle.api.file.*
import org.gradle.api.provider.*

public interface ForeignCLibraries {
    public val includeDirectories: ListProperty<Directory>
    public val libDirectories: ListProperty<Directory>
    // public val binDirectories: ListProperty<Directory> // could be needed for windows only

    public val compilerArguments: ListProperty<String>
    public val linkerArguments: ListProperty<String>

    // ssl, crypto -> -lssl -lcrypto
    // also used in runtime on jvm/android
    public val linkLibraries: ListProperty<String>
    public val embedSharedLibraries: ListProperty<String> // android/jvm
    public val embedStaticLibraries: ListProperty<String> // all targets
}
