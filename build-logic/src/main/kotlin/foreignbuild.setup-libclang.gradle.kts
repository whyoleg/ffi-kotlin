import foreignbuild.dependencies.*

registerTarGzDependencySetupTask(
    "setupClangLibsLinuxX64",
    "foreignbuild.kotlin-native:16.0.0-x86_64-linux:80@tar.gz"
) {
    include("llvm-16.0.0-x86_64-linux-essentials-80/lib/libclang.so.16.0.6")
    eachFile { path = "libclang.so" }
}

registerZipDependencySetupTask(
    "setupClangLibsMingwX64",
    "foreignbuild.kotlin-native:16.0.0-x86_64-windows:56@zip"
) {
    include("llvm-16.0.0-x86_64-windows-essentials-56/lib/libclang.lib")
    eachFile { path = "libclang.lib" }
}

registerTarGzDependencySetupTask(
    "setupClangLibsMacosX64",
    "foreignbuild.kotlin-native:16.0.0-x86_64-macos:56@tar.gz"
) {
    include("llvm-16.0.0-x86_64-macos-essentials-56/lib/libclang.dylib")
    eachFile { path = "libclang.dylib" }
}

registerTarGzDependencySetupTask(
    "setupClangLibsMacosArm64",
    "foreignbuild.kotlin-native:16.0.0-aarch64-macos:65@tar.gz"
) {
    include("llvm-16.0.0-aarch64-macos-essentials-65/lib/libclang.dylib")
    eachFile { path = "libclang.dylib" }
}
