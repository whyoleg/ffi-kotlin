package foreignbuild.dependencies

import foreignbuild.dependencies.tasks.*

val setupClangLibsLinuxX64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz")
    include("llvm-11.1.0-linux-x64-essentials/lib/*.so.11.1")
    eachFile {
        relativePath = RelativePath(true, "libclang.so")
    }
    includeEmptyDirs = false
    into(temporaryDir)
}

val setupClangLibsMacosX64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:apple-llvm-20200714-macos-x64-essentials@tar.gz")
    include("apple-llvm-20200714-macos-x64-essentials/lib/*.dylib")
    eachFile {
        relativePath = RelativePath(true, "libclang.dylib")
    }
    includeEmptyDirs = false
    into(temporaryDir)
}

val setupClangLibsMacosArm64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:apple-llvm-20200714-macos-aarch64-essentials@tar.gz")
    include("apple-llvm-20200714-macos-aarch64-essentials/lib/*.dylib")
    eachFile {
        relativePath = RelativePath(true, "libclang.dylib")
    }
    includeEmptyDirs = false
    into(temporaryDir)
}

val setupClangHeaders by tasks.registering(SetupTarXzDependency::class) {
    dependency("foreignbuild.llvm:clang:11.1.0@src.tar.xz")
    include("clang-11.1.0.src/include/clang-c/*.h")
    eachFile {
        relativePath = RelativePath(true, "clang-c", name)
    }
    includeEmptyDirs = false
    into(temporaryDir)
}
