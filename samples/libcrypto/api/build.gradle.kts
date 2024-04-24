import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    kotlin("multiplatform")
    id("dev.whyoleg.foreign")
    id("foreignbuild.setup-openssl")
}

kotlin {
    jvmToolchain(8)
    jvm()
    macosArm64()
    macosX64()
    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            // TODO: runtime should be added automatically
            api("dev.whyoleg.foreign:foreign-runtime-c")
        }
    }
}

foreign {
    cInterface("libcrypto") {
        initialHeaders.addAll(
            "openssl/evp.h"
        )
        includeHeaders("openssl/**")

        platforms.configureEach {
            targets.configureEach {
                val konanTarget = when (foreignTarget) {
                    ForeignTarget.MacosArm64 -> KonanTarget.MACOS_ARM64
                    ForeignTarget.MacosX64   -> KonanTarget.MACOS_X64
                    ForeignTarget.LinuxX64   -> KonanTarget.LINUX_X64
                    ForeignTarget.MingwX64   -> KonanTarget.MINGW_X64
                    else                     -> error("not supported")
                }
                headerDirectories.add(openssl.v3_2.includeDirectory(konanTarget))
                libraryDirectories.add(openssl.v3_2.libDirectory(konanTarget))
            }
        }

        jvm {
            macosArm64()
            macosX64()
            linuxX64 {

            }
            mingwX64()
        }
        native {
            macosArm64()
            macosX64()
            linuxX64()
            mingwX64()
        }
    }
}
