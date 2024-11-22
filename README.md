# ffi-kotlin

> **EXPERIMENT** for providing Kotlin Multiplatform Foreign Function Interface

WIP, the previous POC is in the [poc](https://github.com/whyoleg/ffi-kotlin/tree/poc) branch.

Testing of ideas is done on a subset of OpenSSL 3.x `libcrypto` API. Support for all kotlin platforms:

* JVM
    * via JNI: min JDK 8
    * via FFM(Panama): min JDK 22
* Android via JNI (minSdk 21)
* Native via cinterop-like solution
* WASM via emscripten
* JS via emscripten
