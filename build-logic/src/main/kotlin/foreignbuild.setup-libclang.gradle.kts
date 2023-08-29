import foreignbuild.dependencies.*

registerTarGzDependencySetupTask(
    "setupClangLibsLinuxX64",
    "foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz"
) {
    include("llvm-11.1.0-linux-x64-essentials/lib/libclang.so.11.1")
    eachFile { path = "libclang.so" }
}

registerZipDependencySetupTask(
    "setupClangLibsMingwX64",
    "foreignbuild.kotlin-native:llvm-11.1.0-windows-x64-essentials@zip"
) {
    include("llvm-11.1.0-windows-x64-essentials/lib/libclang.lib")
    eachFile { path = "libclang.lib" }
}

registerTarGzDependencySetupTask(
    "setupClangLibsMacosX64",
    "foreignbuild.kotlin-native:apple-llvm-20200714-macos-x64-essentials@tar.gz"
) {
    include("apple-llvm-20200714-macos-x64-essentials/lib/libclang.dylib")
    eachFile { path = "libclang.dylib" }
}

registerTarGzDependencySetupTask(
    "setupClangLibsMacosArm64",
    "foreignbuild.kotlin-native:apple-llvm-20200714-macos-aarch64-essentials@tar.gz"
) {
    include("apple-llvm-20200714-macos-aarch64-essentials/lib/libclang.dylib")
    eachFile { path = "libclang.dylib" }
}

registerTarXzDependencySetupTask(
    "setupClangHeaders",
    "foreignbuild.llvm:clang:11.1.0@src.tar.xz"
) {
    include("clang-11.1.0.src/include/clang-c/*.h")
    eachFile { path = "clang-c/$name" }
}
