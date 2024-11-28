package dev.whyoleg.foreign.tooling.clang

public object ClangArguments {
    public fun mingwX64(
        isystemPath: String,
        prefixPath: String,
        sysrootPath: String
    ): List<String> = shared(isystemPath, "x86_64-pc-windows-gnu") + listOf(
        "--prefix=$prefixPath",
        "--sysroot=$sysrootPath"
    )

    public fun linuxX64(
        isystemPath: String,
        prefixPath: String,
        gccToolchainPath: String
    ): List<String> = shared(isystemPath, "x86_64-unknown-linux-gnu") + listOf(
        "--prefix=$prefixPath",
        "--gcc-toolchain=$gccToolchainPath",
        "--sysroot=$gccToolchainPath/x86_64-unknown-linux-gnu/sysroot",
    )

    public fun macosX64(
        isystemPath: String,
        prefixPath: String,
        isysrootPath: String
    ): List<String> = shared(isystemPath, "x86_64-apple-macos10.13") + listOf(
        "--prefix=$prefixPath",
        "-isysroot", isysrootPath
    )

    public fun macosArm64(
        isystemPath: String,
        prefixPath: String,
        isysrootPath: String
    ): List<String> = shared(isystemPath, "arm64-apple-macos10.16") + listOf(
        "--prefix=$prefixPath",
        "-isysroot", isysrootPath
    )

    public fun iosDeviceArm64(
        isystemPath: String,
        prefixPath: String,
        isysrootPath: String
    ): List<String> = shared(isystemPath, "arm64-apple-ios9.0") + listOf(
        "--prefix=$prefixPath",
        "-isysroot", isysrootPath
    )

    public fun iosSimulatorArm64(
        isystemPath: String,
        prefixPath: String,
        isysrootPath: String
    ): List<String> = shared(isystemPath, "arm64-apple-ios9.0-simulator") + listOf(
        "--prefix=$prefixPath",
        "-isysroot", isysrootPath
    )

    public fun iosSimulatorX64(
        isystemPath: String,
        prefixPath: String,
        isysrootPath: String
    ): List<String> = shared(isystemPath, "x86_64-apple-ios9.0-simulator") + listOf(
        "--prefix=$prefixPath",
        "-isysroot", isysrootPath
    )

    private fun shared(
        isystemPath: String,
        llvmTarget: String
    ): List<String> = listOf(
        // TODO: are those 2 parameters needed?
        "-fno-stack-protector", "-fexceptions",
        "-target", llvmTarget,
        "-isystem", isystemPath
    )
}

