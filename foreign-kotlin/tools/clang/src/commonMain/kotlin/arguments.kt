package dev.whyoleg.foreign.tool.clang

// TODO: recheck arguments on each LLVM/kotlin update
internal fun clangCompilerArguments(
    target: ClangTarget,
    options: Map<ClangOption, String>
): List<String> {
    // options
    fun optional(option: ClangOption): String? = options[option] ?: option.default
    fun required(option: ClangOption): String = requireNotNull(optional(option)) { "Option '$option' is required" }

    val llvmPath = required(ClangOption.LLVM_PATH)
    val llvmVersion = required(ClangOption.LLVM_VERSION)
    val isystemPath = "$llvmPath/lib/clang/$llvmVersion/include"
    val prefixPath = "$llvmPath/usr/bin"

    val arguments = mutableListOf(
        "-target", target.llvmTarget,
        "-isystem", isystemPath,
        "--prefix=$prefixPath",
        // TODO: are those 2 parameters needed?
        "-fno-stack-protector", "-fexceptions"
    )

    // target specific arguments
    arguments.addAll(
        when (target) {
            ClangTarget.MingwX64          -> {
                val mingwToolchainPath = required(ClangOption.MINGW_X64_TOOLCHAIN_PATH)
                listOf(
                    "--sysroot=${mingwToolchainPath}"
                )
            }

            ClangTarget.LinuxX64          -> {
                val gccToolchainPath = required(ClangOption.LINUX_X64_TOOLCHAIN_PATH)
                listOf(
                    "--gcc-toolchain=$gccToolchainPath",
                    "--sysroot=$gccToolchainPath/${target.llvmTarget}/sysroot",
                )
            }

            ClangTarget.LinuxArm64          -> {
                val gccToolchainPath = required(ClangOption.LINUX_ARM64_TOOLCHAIN_PATH)
                listOf(
                    "--gcc-toolchain=$gccToolchainPath",
                    "--sysroot=$gccToolchainPath/${target.llvmTarget}/sysroot",
                )
            }

            ClangTarget.MacosArm64        -> {
                val macosSdkPath = required(ClangOption.MACOS_SDK_PATH)
                listOf(
                    "-isysroot", macosSdkPath
                )
            }

            ClangTarget.MacosX64          -> {
                val macosSdkPath = required(ClangOption.MACOS_SDK_PATH)
                listOf(
                    "-isysroot", macosSdkPath
                )
            }

            ClangTarget.IosDeviceArm64    -> {
                val iosDeviceSdkPath = required(ClangOption.IOS_DEVICE_SDK_PATH)
                listOf(
                    "-isysroot", iosDeviceSdkPath
                )
            }

            ClangTarget.IosSimulatorArm64 -> {
                val iosSimulatorSdkPath = required(ClangOption.IOS_SIMULATOR_SDK_PATH)
                listOf(
                    "-isysroot", iosSimulatorSdkPath
                )
            }

            ClangTarget.IosSimulatorX64   -> {
                val iosSimulatorSdkPath = required(ClangOption.IOS_SIMULATOR_SDK_PATH)
                listOf(
                    "-isysroot", iosSimulatorSdkPath
                )
            }
        }
    )

    return arguments
}
