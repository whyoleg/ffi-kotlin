package foreignbuild.dependencies

import foreignbuild.dependencies.tasks.*

val setupLibclangLinuxX64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:llvm-11.1.0-linux-x64-essentials@tar.gz")
    include("llvm-11.1.0-linux-x64-essentials/lib/*.so.11.1")
    eachFile {
        relativePath = RelativePath(true, "libclang.so")
    }
    includeEmptyDirs = false
    into(layout.buildDirectory.dir("libclang/lib/linux-x64"))
}

val setupLibclangMacosX64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:apple-llvm-20200714-macos-x64-essentials@tar.gz")
    include("apple-llvm-20200714-macos-x64-essentials/lib/*.dylib")
    eachFile {
        relativePath = RelativePath(true, "libclang.dylib")
    }
    includeEmptyDirs = false
    into(layout.buildDirectory.dir("libclang/lib/macos-x64"))
}

val setupLibclangMacosArm64 by tasks.registering(SetupTarGzDependency::class) {
    dependency("foreignbuild.kotlin-native:apple-llvm-20200714-macos-aarch64-essentials@tar.gz")
    include("apple-llvm-20200714-macos-aarch64-essentials/lib/*.dylib")
    eachFile {
        relativePath = RelativePath(true, "libclang.dylib")
    }
    includeEmptyDirs = false
    into(layout.buildDirectory.dir("libclang/lib/macos-arm64"))
}

val setupLibclangHeaders by tasks.registering(SetupTarXzDependency::class) {
    dependency("foreignbuild.llvm:clang:11.1.0@src.tar.xz")
    include("clang-11.1.0.src/include/clang-c/*.h")
    eachFile {
        relativePath = RelativePath(true, "clang-c", name)
    }
    includeEmptyDirs = false
    into(layout.buildDirectory.dir("libclang/include"))
}
