package dev.whyoleg.foreign.cx.playground

// TODO: use okio path?
internal sealed class CxIndexGeneratorTarget(
    val llvmTarget: String,
) {
    class KonanDirectory(
        val root: String
    ) {
        val dependencies get() = "$root/dependencies"
        fun dependency(path: String): String = "$dependencies/$path"

        //TODO: other hosts usage
        fun llvmDirectory(host: Host): String = when (host) {
            Host.MacosArm64 -> dependency("apple-llvm-20200714-macos-aarch64-essentials")
            Host.MacosX64   -> TODO()
            Host.WindowsX64 -> TODO()
            Host.LinuxX64   -> TODO()
        }
    }

    enum class Host {
        MacosArm64,
        MacosX64,
        WindowsX64,
        LinuxX64
    }

    fun arguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
        "-target", llvmTarget,
        "-fno-stack-protector", "-fexceptions",
        "-isystem", "${konanDirectory.llvmDirectory(host)}/lib/clang/11.1.0/include"
    ) + customArguments(host, konanDirectory)

    protected abstract fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String>

    object MingwX64 : CxIndexGeneratorTarget("x86_64-pc-windows-gnu") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "--sysroot=${konanDirectory.dependency("msys2-mingw-w64-x86_64-2")}"
        )
    }

    object LinuxX64 : CxIndexGeneratorTarget("x86_64-unknown-linux-gnu") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> {
            val gcc = konanDirectory.dependency("x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2")
            return listOf(
                "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
                "--gcc-toolchain=${gcc}",
                "--sysroot=$gcc/$llvmTarget/sysroot",
            )
        }
    }

    object MacosArm64 : CxIndexGeneratorTarget("arm64-apple-macos10.16") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            // or "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "-isysroot", "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
        )
    }

    object MacosX64 : CxIndexGeneratorTarget("x86_64-apple-macos10.13") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            // or "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "-isysroot", "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX.sdk",
        )
    }

    object IosDeviceArm64 : CxIndexGeneratorTarget("arm64-apple-ios9.0") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            // or "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "-isysroot", "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS.sdk",
        )
    }

    object IosSimulatorArm64 : CxIndexGeneratorTarget("arm64-apple-ios9.0-simulator") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            // or "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "-isysroot", "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
        )
    }

    object IosSimulatorX64 : CxIndexGeneratorTarget("x86_64-apple-ios9.0-simulator") {
        override fun customArguments(host: Host, konanDirectory: KonanDirectory): List<String> = listOf(
            // or "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
            "-B${konanDirectory.llvmDirectory(host)}/usr/bin",
            "-isysroot", "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator.sdk",
        )
    }
}
