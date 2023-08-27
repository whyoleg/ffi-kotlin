package dev.whyoleg.foreign.cx.index.generator


val mingw = """
        -B/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/usr/bin
        -fno-stack-protector
        -target
        x86_64-pc-windows-gnu
        --sysroot=/Users/whyoleg/.konan/dependencies/msys2-mingw-w64-x86_64-2
    """.trimIndent()

val linux = """
        -B/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/usr/bin
        -fno-stack-protector
        --gcc-toolchain=/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2
        -target
        x86_64-unknown-linux-gnu
        --sysroot=/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/x86_64-unknown-linux-gnu/sysroot
        -fPIC
    """.trimIndent()

val macosX64 = """
        -B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin
        -fno-stack-protector
        -target
        x86_64-apple-macos10.13
        -isysroot
        /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX13.3.sdk
        -fPIC
    """.trimIndent()

val macosArm64 = """
        -B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin
        -fno-stack-protector
        -target
        arm64-apple-macos10.16
        -isysroot
        /Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX13.3.sdk
        -fPIC
    """.trimIndent()

val iosArm64 = """
        -B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin
        -fno-stack-protector
        -target
        arm64-apple-ios9.0
        -isysroot
        /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS16.4.sdk
        -fPIC
    """.trimIndent()

// sim
val iosX64 = """
        -B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin
        -fno-stack-protector
        -target
        x86_64-apple-ios9.0-simulator
        -isysroot
        /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator16.4.sdk
        -fPIC
    """.trimIndent()

val iosSimulatorArm64 = """
        -B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin
        -fno-stack-protector
        -target
        arm64-apple-ios9.0-simulator
        -isysroot
        /Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator16.4.sdk
        -fPIC
    """.trimIndent()
