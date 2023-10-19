Workflow of gradle plugin:

tasks:

`generate CxCompilerIndex Libcrypto Jvm MacosArm64`
`generate CxCompilerIndex Libcrypto Native MacosArm64`

build/foreign/interfaces/libcrypto/index/jvm-macosArm64.json
build/foreign/interfaces/libcrypto/index/native-macosArm64.json

`generate CxBridgeFragments Libcrypto`
output per sourceSet - for now single task, but can be divided into task hierarchy later if needed
build/foreign/interfaces/libcrypto/fragments/jvm.json
build/foreign/interfaces/libcrypto/fragments/macosArm64.json
build/foreign/interfaces/libcrypto/fragments/native.json
build/foreign/interfaces/libcrypto/fragments/common.json

`generate CxBridgeSources Libcrypto`
generate all needed sources (kotlin, c, other files) - for now single task, but can be divided into task hierarchy later
if needed
build/foreign/interfaces/libcrypto/sources/jvm/kotlin/*.kt
build/foreign/interfaces/libcrypto/sources/jvm/c/*.c
build/foreign/interfaces/libcrypto/sources/jvm/
build/foreign/interfaces/libcrypto/sources/macosArm64/kotlin
build/foreign/interfaces/libcrypto/sources/macosArm64/c
build/foreign/interfaces/libcrypto/sources/native/kotlin
build/foreign/interfaces/libcrypto/sources/common/kotlin

we have:

* jvm - macosArm64 + macosX64
* native - macosArm64 + macosX64
* Dynamic linking

1. generate 4 indexes
2. generate fragments
3. generate sources
4. build 2 JNI libs and add them to resources
5. build 2 native bitcodes and add them to task
6. add other compiler arguments to K/N compiler
