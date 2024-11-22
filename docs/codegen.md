# What need to be generated

/shared*/kotlin - shared kotlin code (expects), `shared` sourceSet = common/native/apple/etc
/jvm/kotlin - target specific kotlin code (actuals)
/jvm/c - C code for JNI
/jvm/resources - ?
/android/kotlin - target specific kotlin code
/android/c - C code for JNI
/android/jni - JNI Android.mk file
/js/kotlin
/js/c
/wasmJs/kotlin
/wasmJs/c
/macosArm64/kotlin
/macosArm64/c

TODO:

1. install dependencies (conan)
2. generate declarations index based on headers (clang)
3. commonize (optional)
4. generate code (kotlin, c, etc)
5. wire everything via Gradle plugin

POC: JVM target for both JNI and FFM.

steps:

* generate code for JVM (current OS)
* generate code for WasmWasi/WasmJs/Js
* generate code for JVM (multi OS)

// indexes: (platform-target)
// jvm-macosArm64
// jvm-macosX64
// android-androidArm64
// android-androidX64
// native-macosArm64
// native-macosX64
// js-wasm
// wasmJs-wasm
// etc...

// group 1 = jvm-macosArm64 * jvm-macosX64 = jvm - single fragment
// group 2 = native-macosArm64 * native-macosX64 = macos + macosArm64-partial + macosX64-partial - 3 fragments

//jvm = merge(jvm1, jvm2)
//android = merge(android1, android2)
//jvmAndroid = commonize(jvm, android)

//macos = commonize(macos1, macos2)
//native = commonize(macos, linux, mingw)
//common = commonize(jvmAndroid, native, wasmJs)

bridgegen operations:

* diff
* merge
* split

indexes -(convert to fragment)-> fragments -(merge or commonize)-> fragments -(codegen)-> code
