plugins {
    alias(kotlinLibs.plugins.multiplatform)
    id("dev.whyoleg.foreign")

    id("foreignbuild.setup-openssl3")
}

kotlin {
    jvm()
    macosArm64()
}

foreign {
    interfaces {
        cx("libcrypto") {
            libraryLinkageNames.add("crypto")
            bindings {
                initialHeaders.addAll(
                    "openssl/evp.h"
                )
                includeHeaders { it.startsWith("openssl/") }
                packageName {
                    when {
                        it.startsWith("openssl/") -> "foreign.bindings.openssl"
                        else                      -> "foreign.bindings.openssl.internal"
                    }
                }
            }
            jvm {
                macosArm64 {
                    includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
                    libraryDirectories.add(openssl3.libDirectory("macos-arm64"))
                }
            }
            native {
                macosArm64 {
                    includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
                    libraryDirectories.add(openssl3.libDirectory("macos-arm64"))
                }
            }
//            android {
//                arm64 {
//                    includeDirectories.add(openssl3.includeDirectory("android-arm64"))
//                    libraryDirectories.add(openssl3.libDirectory("android-arm64"))
//                }
//            }
        }
    }
}
