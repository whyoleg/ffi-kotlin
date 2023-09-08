## Gradle plugin

### JNI (no common)

Workflow:

- generate C sources
    - build JNI library
    - put JNI library into resources
- generate Kotlin sources
    - link kotlin sources to sourceSet

TODO:

- decide on `long` on Windows (it's 32bit even on 64bit systems)
- what to do with declarations that are available on JVM per platform, f.e. only on Windows or macOS
- how to distribute JNI library when it's built on different machines:
- it looks like we need to somehow use gradle metadata for per platform builds,
  similar to how kotlin plugin uses it for native targets but more complex

Parameters:

- include paths - for JNI + declaration generation
- link paths - for JNI
- library loading - shared / prebuilt / none
- bindings kind - JNI / FFM / JNI+FFM
- headers to index
- filters for indexing
- visibility/packages, etc
- package for bindings
- name of the produced bindings library (should be unique for package)
- configure for compilation (as in cinterop) or for sourceSet or for target?
-

packages:

* runtime - dev.whyoleg.foreign.*
* tools - dev.whyoleg.foreign.tools.*
* gradle plugin - dev.whyoleg.foreign.gradle.*
* compiler plugin - dev.whyoleg.foreign.compiler.*
* idea plugin - dev.whyoleg.foreign.idea.*

Download K/N LLVM:

* https://github.com/llvm/llvm-project/releases/download/llvmorg-11.1.0/clang-11.1.0.src.tar.xz
* https://github.com/JetBrains/kotlin/blob/1.9.0/kotlin-native/konan/konan.properties
* https://download.jetbrains.com/kotlin/native/apple-llvm-20200714-macos-aarch64-essentials.tar.gz
* https://download.jetbrains.com/kotlin/native/apple-llvm-20200714-macos-x64-essentials.tar.gz
* https://download.jetbrains.com/kotlin/native/llvm-11.1.0-linux-x64-essentials.tar.gz
* https://download.jetbrains.com/kotlin/native/llvm-11.1.0-windows-x64-essentials.zip

Download JDK

* https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-macosx_x64.zip
* https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-macosx_aarch64.zip
* https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-linux_x64.zip
* https://cdn.azul.com/zulu/bin/zulu8.72.0.17-ca-jdk8.0.382-win_x64.zip

Usage:

Manual:

1. manually write declaration with annotations in source code
2. run compiler plugin
    - will fetch metadata from source code
    - will generate platform-specific code

Bindings generation:

1. provide headers
2. run cx generator - will generate cx-index of parsed declarations (cx-index-generator)
3. run source generator - will generate code based on cx-index (with visibility/optIn/filters apllied) (cx-X-generator)
4. run compiler plugin
    - will fetch metadata from source code (cx-X-metadata)
    - will generate platform-specific code

on gradle plugin:

- bindings generation
    - inputs:
        - headers
        - include dirs
        - package/visibility
    - outputs:
        - kotlin code
        - c code
- compilation setup
    - jni (jvm/android)
    - native bitcode compilation
    - wasm/js - reevaluate possibilities

## Bindings

1. generate bindings from index
2. diff of bindings (needed for both combining and splitting)
3. combine N per host JVM bindings into single bindings
    - input: N bindings per host
    - output: single shared bindings + N host-specific bindings (combined afterward in single artifact)
4. combine N per abi android bindings into single bindings
5. split N bindings into: N+1 specific+shared bindings
    - input: N bindings per 'target'
    - output: N 'target'-specific bindings + shared bindings
