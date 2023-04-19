# ffi-kotlin

> **EXPERIMENT** for providing Kotlin Multiplatform Foreign Function Interface

Support for all kotlin platforms:

* JVM
    * via JNI: min JDK 8
    * via FFM(Panama):
        * strictly JDK 20 (JEP: https://openjdk.org/jeps/434)
        * in future (somewhere in August 2023, based on https://openjdk.org/projects/jdk/21/)
          strictly JDK 21 (JEP: https://openjdk.org/jeps/442)
* Android via JNI (minSdk 21)
* Native via cinterop-like solution
* WASM via emscripten
* JS via emscripten

## Evaluation

For now, testing of ideas is done on a subset of OpenSSL 3.x `libcrypto` API:
Modules are placed inside `libraries/libcrypto3` directory:

* api - expect/actual declarations for some part of `libcrypto` API
* shared - dynamically linked artifacts - only native desktop and jvm targets
* prebuilt - library is embedded inside artifact
* test - some basic tests which tests that everything REALLY works

## Foreign Function Interface

### JVM - FFM(Panama)

Uses similar code that could generate jextract but using some kotlin specifics.
Only Kotlin codegen and JDK 20 are needed.

### JVM/Android - JNI

Uses similar code that is used in K/N jvm cinterop flavor.
To use such bindings, we need:

1. generate Kotlin code
2. generate C code
3. build JNI C library using some compiler for each OS
   (not sure, what is the best workflow here regarding portability and cross-compilation)
4. somehow package it - both per OS+ARCH and single artifact for all OSs

For Android, everything is the same,
but building and packaging are not a problem
as NDK is able to cross-compile
and an AAR format supports distributing shared libraries for multiple architectures

TODO:

* requires compiler to build JNI library - how to set up it properly to support different OSs?
* is it possible to use K/N provided clang with cross-compile possibilities here?
* replace ByteBuffers with Unsafe but with safe fallback to ByteBuffers if Unsafe is not available
  (similar to how netty handles it)
* somehow fix duplicating service in resources for android

### WASM/JS - emscripten

Use emscripten to generate WASM binary from C library
To use such bindings we need:

1. generate Kotlin code
2. generate C code
3. build WASM binary using emscripten (`emcc` can be run on any supported OS)
4. somehow package it (still not sure how)

TODO:

* how to set up packaging, so it will work all the time?
* how to support memory sharing between different libraries?
* how to publish wasm+js with the kotlin library, so it will be consumable?
* a lot of hacks to attach prebuilt dependency...
* setup testing in browser
* WASM for now requires JS to launch, need to support WASI in future somehow

### Native

How to use C function from K/N without cinterop:

* generate C code:
  https://github.com/JetBrains/kotlin/blob/e10e821cd47449ae2b0b4f83d34baacb84f176c6/kotlin-native/Interop/StubGenerator/src/main/kotlin/org/jetbrains/kotlin/native/interop/gen/CWrapperGenerator.kt
* build bitcode:
  https://github.com/JetBrains/kotlin/blob/b6fdc2dbfc51697c0ea73bbb29bf1234b224fc87/kotlin-native/Interop/StubGenerator/src/main/kotlin/org/jetbrains/kotlin/native/interop/gen/jvm/main.kt#L417
* embed bitcode via K/N compiler args: `-native-library <path to bitcode file>`

## Foreign Libraries Linking

Overview of current status:

* There are 2 ways to distribute FFI:
    * **SHARED** - native library should be available at **RUNTIME** in some system directory
    * **PREBUILT** - native library is embedded inside artifact
* There are 2 types of native libraries used during build:
    * **DYNAMIC**
    * **STATIC**
* There are 2(3) ways to load(link) library:
    * at **RUNTIME**
    * at **BUILD TIME**
    * both

### JVM(both FFM and JNI)

* Support both **SHARED** and **PREBUILT** variants.
* For both variants, **DYNAMIC** libraries are used
* Loading(linking) is done at **RUNTIME** via `LibraryLoader` which under the hood uses `System.loadLibrary` to load lib from JAR or system.
    * for JNI we still need to additionally link at **BUILD TIME**

### Android

* Only **PREBUILT** variant is supported.
* **DYNAMIC** libraries are used
* Loading(linking) is done at **RUNTIME** via `LibraryLoader` which under the hood uses `System.loadLibrary`
  and JNI is linked/built at **BUILD TIME**

### WASM/JS

* Only **PREBUILT** variant is supported.
* **STATIC** libraries are used
* Loading(linking) is done via hacks at both **BUILD TIME** and **RUNTIME** :)

### Native (desktop targets)

* Support both **SHARED** and **PREBUILT** variants
* **STATIC** libraries are used for **PREBUILT** variant and **DYNAMIC** is used for **SHARED** variant
* Loading(linking) is done at **BUILD TIME** via cinterop(?)/compiler flags
* When **SHARED** artifact is consumed by another project, it should have libraries available to be able to LINK final binary

### Native (ios)

* Only **PREBUILT** variant is supported.
* **STATIC** libraries are used
* Loading(linking) is done at **BUILD TIME** via cinterop(?)/compiler flags

## Current plans

### New multiplatform runtime

Introduce new runtime, which will be separate from kotlinx.cinterop with better API and better multiplatform support.

### Compiler plugin

From the beginning, plugin should allow:

* for annotated functions, generate multiplatform glue code which will allow to call C functions, f.e.:
  ```kotlin
  @ForeignCCall
  external fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
  ): CPointer<EVP_MAC>?
  ```
* for annotated structs, generate multiplatform glue code which will allow to allocate struct and access it properties, f.e.:
  ```kotlin
  @ForeignCStruct
  external class OSSL_PARAM {
    var key: CString?
    var data_type: UInt
    var data: CPointer<Unit>?
    var data_size: PlatformUInt
    var return_size: PlatformUInt
  }
  ``` 

### Auto-generation of bindings from headers

Generator will produce declarations with annotations, which will then be processed by compiler plugin.

As a first step, the idea is using original cinterop to generate declarations for clang index for some K/N target,
write minimal generator that will work for `libcrypto` sample and `libclang`.
Then generate declarations and continue work with JVM (faster dev cycle) while still using common code
(at least to be able to run it later in K/N - why? just why not)

## Future project structure: which is how I see it now

* RUNTIME:
    * foreign-core - provides main primitives to work with foreign memory and libraries
    * foreign-runtime-c - provides primitives for interoperability with C language. f.e CPointer, CArray, CStruct, CString, etc
* BUILD:
    * foreign-compiler-plugin - based on annotations (like `ForeignCCall` or `ForeignCStruct`) will generate glue code
      for interacting with foreign functions
    * foreign-gradle-plugin - provides a pluggable system to fetch (f.e. via `conan`), build and link (f.e. via `clang` or `emcc`)
      foreign(native) libraries to multiplatform code.
      Additionally, install the compiler plugin and provide configuration for it.
    * PACKAGE MANAGEMENT (used by gradle/idea plugin)
        * foreign-package-manager-api
        * foreign-package-manager-conan - integration with https://conan.io for providing/building native libraries
        * foreign-package-manager-vcpkg
        * foreign-package-manager-brew
        * foreign-package-manager-nix (?)
        * foreign-package-manager-manual
    * COMPILER/LINKER
        * foreign-compiler-api
        * foreign-compiler-emscripten
        * foreign-compiler-clang
        * foreign-compiler-gcc
        * foreign-compiler-kn - uses K/N toolchain (as it provides cross-compilation capabilities)
    * C INTEROP BINDINGS GENERATION:
        * foreign-cx-index - provides models that are retrieved from C headers via `libclang`
        * foreign-cx-indexer - provides K/N(or MPP in future) CLI to retrieve data from C headers via `libclang`
* foreign-idea-plugin - in future may provide additional functionality, like:
    * ctrl+click from declaration to C header (if available)
    * C2K converter (paste C code -> convert to Kotlin interop code) using `libclang`

## Future ideas

* somehow integrate with local/class properties via property reference.
    * Needs https://youtrack.jetbrains.com/issue/KT-28346
    * Somehow, it's needed to limit the lifecycle of property reference to a function scope
