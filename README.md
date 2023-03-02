# ffi-kotlin

> **EXPERIMENT** for providing Kotlin Multiplatform Foreign Function Interface

Support for all kotlin platforms:

* JVM via JNI (min JDK 8) and via Panama (min JDK 20)
* Android via JNI (minSdk 21)
* Native via cinterop
* WASM via emscripten
* JS via emscripten

### Structure

* ffi-c - kotlinx.cinterop package adapted for multiplatform
* libraries
    * libcrypto3
        * api - expect/actual declarations for some part of OpenSSL libcrypto API
        * shared - dynamically linked artifacts - only native and jvm desktop targets
        * prebuilt - library is embedded and no additional configuration is needed
        * test - some basic tests which tests, that everything REALLY works

### Implementation details

For future auto-generated bindings we need to keep in mind platform details

#### JVM+Panama

Uses similar code, that could generate jextract but using some kotlin specifics.
Only Kotlin codegen and JDK 20 is needed.

#### JVM(Android)+JNI

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

#### WASM/JS

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

#### Native

Nothing interesting, just mapping for cinterop

### Auto-generation like in cinterop

TBD - a lot of work is needed here.
As first step, the idea is using original cinterop to generate declarations for clang index for some K/N target,
write minimal generator that will work for libcrypto3 sample and clang index.
Then generate declarations and continue work with JVM (faster dev cycle) while still using common code
(at least to be able to run it later in K/N - why? just why not)

### Future ideas

* integration with https://conan.io for providing/building native libraries
