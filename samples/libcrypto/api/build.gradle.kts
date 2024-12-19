import dev.whyoleg.foreign.gradle.dsl.*
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
}

foreign {
    c("libcrypto") {
        jvm {
            // targets?
            macosArm64()
            macosX64()
            linuxX64()
            mingwX64()
        }

        native {
            macosArm64()
            macosX64()
            linuxX64()
            mingwX64()
        }

        js {
            wasm()
        }
        wasmJs {
            wasm()
        }

        foreignTargets.configureEach {
            // controls what to generate
            bindings {
                packageName { header ->
                    when {
                        header.startsWith("openssl") -> "libcrypto"
                        else                         -> "libcrypto.internal"
                    }
                }
//                packageName(
//                    "openssl/**" to "libcrypto",
//                    "**" to "libcrypto.internal"
//                )
                initialHeaders("openssl/evp.h")
                includeHeaders("openssl/**")
                excludeHeaders("XXX")
            }
            // controls how to build
            libraries {
                val konanTarget = when (foreignTargetType) {
                    ForeignTargetType.MacosArm64 -> KonanTarget.MACOS_ARM64
                    ForeignTargetType.MacosX64   -> KonanTarget.MACOS_X64
                    ForeignTargetType.LinuxX64   -> KonanTarget.LINUX_X64
                    ForeignTargetType.MingwX64   -> KonanTarget.MINGW_X64
                    else                         -> error("not supported")
                }
                includeDirectories.add(openssl.v3_2.includeDirectory(konanTarget))
                libDirectories.add(openssl.v3_2.libDirectory(konanTarget))

                // linkLibraries.add("crypto")
                embedStaticLibraries.add("libcrypto.a")
            }
        }
    }
}
