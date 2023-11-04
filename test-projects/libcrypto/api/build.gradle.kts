plugins {
    alias(kotlinLibs.plugins.multiplatform)
//    id("dev.whyoleg.foreign")

    id("foreignbuild.setup-openssl3")
}

kotlin {
    jvm()
    macosArm64()
    macosX64()
}

//foreign {
//    cxInterops.create("libcrypto") {
//        libraryLinkageNames.add("crypto")
//        initialHeaders.addAll(
//            "openssl/evp.h"
//        )
//        includeHeaders { it.startsWith("openssl/") }
//        packageName {
//            when {
//                it.startsWith("openssl/") -> "foreign.bindings.openssl"
//                else -> "foreign.bindings.openssl.internal"
//            }
//        }
//        jvm {
//            macosArm64 {
//                includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
//                librarySearchDirectories.add(openssl3.libDirectory("macos-arm64"))
//            }
//            macosX64 {
//                includeDirectories.add(openssl3.includeDirectory("macos-x64"))
//                librarySearchDirectories.add(openssl3.libDirectory("macos-x64"))
//            }
//        }
//        native {
//            macosArm64 {
//                includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
//                librarySearchDirectories.add(openssl3.libDirectory("macos-arm64"))
//            }
//            macosX64 {
//                includeDirectories.add(openssl3.includeDirectory("macos-x64"))
//                librarySearchDirectories.add(openssl3.libDirectory("macos-x64"))
//            }
//        }
//        android {
//            arm64 {
//                includeDirectories.add(openssl3.includeDirectory("android-arm64"))
//                librarySearchDirectories.add(openssl3.libDirectory("android-arm64"))
//            }
//        }
//    }
//}
