# ffi-kotlin - kotlinx.cinterop but multiplatform

**EXPERIMENT** for providing Kotlin Multiplatform Foreign Function Interface

Supports:

* JVM via JNI and Panama (JDK 20)
* Native via cinterop
* WASM/JS via emscripten

### Structure

* ffi-runtime - kotlinx.cinterop package adapted for multiplatform
* libraries/libcrypto3
    * api - expect/actual declarations for some part of OpenSSL libcrypto API
    * dynamic/static - linking
    * test - some basic tests which tests, that everything REALLY works

### Implementation details

For future auto-generated bindings we need to keep in mind platform details

#### JVM+Panama

Uses similar code, that could generate jextract but using some kotlin specifics.
Only Kotlin codegen and JDK 20-ea is needed.

#### JVM+JNI

Uses similar code, that is used in K/N jvm cinterop flavor.
To use such bindings we need:

1. generate Kotlin code
2. generate C code
3. build JNI C library using some compiler for each OS (not sure, what is the best workflow here)
4. somehow package it

#### WASM/JS

Uses emscripten to generate WASM binary from C library
and some hacks to be able to run tests with it :)
To use such bindings we need:

1. generate Kotlin code
2. generate C code
3. build WASM binary using emscripten (can be run on any OS)
4. somehow package it (still not sure how)

#### Native

nothing interesting, just mapping for cinterop (though tested for now only with macos-arm64)

### Auto-generation like in cinterop

TBD - a lot of work is needed here.
As first step, the idea is using original cinterop to generate declarations for clang index for some K/N target,
write minimal generator that will work for libcrypto3 sample and clang index.
Then generate declarations and continue work with JVM (faster dev cycle) while still using common code
(at least to be able to run it later in K/N - why? just why not)
