# ffi-kotlin

> **EXPERIMENT** for providing Kotlin Multiplatform Foreign Function Interface

Support for all kotlin platforms:

* JVM
    * via JNI: min JDK 8
    * via FFM(Panama): min JDK 20 (JEP: https://openjdk.org/jeps/434)
* Android via JNI (minSdk 21)
* Native via cinterop
* WASM via emscripten
* JS via emscripten

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
write minimal generator that will work for libcrypto3 sample and clang index.
Then generate declarations and continue work with JVM (faster dev cycle) while still using common code
(at least to be able to run it later in K/N - why? just why not)

## Structure

* ffi-c-runtime - kotlinx.cinterop package adapted for multiplatform
* libraries
    * libcrypto3
        * api - expect/actual declarations for some part of OpenSSL libcrypto API
        * shared - dynamically linked artifacts - only native and jvm desktop targets
        * prebuilt - library is embedded and no additional configuration is needed
        * test - some basic tests which tests, that everything REALLY works

## Foreign Function Interface

For future auto-generated bindings we need to keep in mind platform details

### JVM - FFM(Panama)

Uses similar code, that could generate jextract but using some kotlin specifics.
Only Kotlin codegen and JDK 20 is needed.

### JVM/Android - JNI

Uses similar code, that is used in K/N jvm cinterop flavor.
To use such bindings we need:

1. generate Kotlin code
2. generate C code
3. build JNI C library using some compiler for each OS
   (not sure, what is the best workflow here regarding portability and cross-compilation)
4. somehow package it - both per OS+ARCH and single artifact for all OSs

For Android everything is the same
but building and packaging is not a problem
as NDK is able to cross-compile
and AAR format supports distributing shared libraries for multiple architectures

TODO:

* requires compiler to build JNI library - how to set up it properly to support different OSs?
* is it possible to use K/N provided clang with cross-compile possibilities here?
* replace ByteBuffers with Unsafe but with safe fallback to ByteBuffers if Unsafe is not available
  (similar to how netty handles it)
* somehow fix duplicating service in resources for android

### WASM/JS - emscripten

Uses emscripten to generate WASM binary from C library
To use such bindings we need:

1. generate Kotlin code
2. generate C code
3. build WASM binary using emscripten (`emcc` can be run on any supported OS)
4. somehow package it (still not sure how)

TODO:

* how to set up packaging, so it will work all time?
* JS uses hardcoded emscripten library name in external declarations - how to avoid it?
* how to support memory sharing between different libraries?
* how to publish wasm+js with kotlin library, so it will be consumable?
* a lot of hacks to attach prebuilt dependency...
* setup testing in browser
* WASM for now require JS to launch, need to support WASI in future somehow

### Native

Nothing interesting, just mapping for cinterop

## Foreign Libraries Linking

Overview:

* There are 2 ways to distribute FFI:
    * **SHARED** - native library should be available at **RUNTIME** in some system directory
    * **PREBUILT** - native library is embedded inside artifact
* There are 2 types of native libraries used during build:
    * **DYNAMIC**
    * **STATIC**
* There are 2(3) ways to load(link) library:
    * at **RUNTIME**
    * at **BUILD TIME**

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

## Future ideas

* somehow integrate with local/class properties via property reference
* integration with https://conan.io for providing/building native libraries
* some idea plugin that will do C2K conversion (paste C code -> convert to Kotlin interop code) using clang api
