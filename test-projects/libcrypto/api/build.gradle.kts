plugins {
    alias(kotlinLibs.plugins.multiplatform)
    id("dev.whyoleg.foreign")

    id("foreignbuild.setup-openssl3")
}

kotlin {
    jvm()
    macosArm64()
    macosX64()
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
                macosX64 {
                    includeDirectories.add(openssl3.includeDirectory("macos-x64"))
                    libraryDirectories.add(openssl3.libDirectory("macos-x64"))
                }
            }
            native {
                macosArm64 {
                    includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
                    libraryDirectories.add(openssl3.libDirectory("macos-arm64"))
                }
                macosX64 {
                    includeDirectories.add(openssl3.includeDirectory("macos-x64"))
                    libraryDirectories.add(openssl3.libDirectory("macos-x64"))
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

val openssl = foreignDependencies.raw("openssl") {
    macosArm64 {
        includeDirectories.add(openssl3.includeDirectory("macos-arm64"))
        libraryDirectories.add(openssl3.libDirectory("macos-arm64"))
    }
    macosX64 {
        includeDirectories.add(openssl3.includeDirectory("macos-x64"))
        libraryDirectories.add(openssl3.libDirectory("macos-x64"))
    }
}

foreignInterfaces.cx("libcrypto") {
    // TODO: how to better configure it
    // it will be injected in this compilation tree (main, integrationTest, test, etc)
    compilationTree.set("main")
    packageName.set("")

    jvm {
        runtimeKind = JNI
        macosArm64()
        macosX64()
    }
    js {
        nodejs()
    }
    native {
        macosArm64()
        macosX64()
    }
    wasm {
        nodejs()
    }

    bindings {
        // optIn, public, etc
        // initialHeaders, header filters, etc

        macosArm64 {

        }
    }
}
