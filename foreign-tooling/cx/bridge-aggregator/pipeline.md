test project:

indexes: 3 desktop, 2 android, 3 ios, 1 wasm = 9 - input
kotlin targets: 2 jvm-like, 7 native, 2 web = 11 - intermediate
source sets: 18

- common
    - jvmCommon
        - jvm (macosArm64 + macosX64)
        - android (arm64+x64)
    - native
        - apple
            - macos
                - macosX64
                - macosArm64
            - ios
                - 3 targets
        - linux
        - mingw
        - TODO: desktop
    - emscripten (web)
        - wasmJs
        - js

targets:

- jvm (single artifact) - jni and/or panama
    - macos-x64
    - macos-arm64
    - linux-x64
    - mingw-x64 or windows-x64 - TBD
- android (single artifact) - jni only
    - arm64
    - x64
    - ...
- wasmWasi (nodejs and browser?)
    - wasm32 or wasm64 or?
- wasmJs (nodejs and browser)
    - wasm32 or wasm64
- js (nodejs and browser)
    - wasm32 or wasm64
- native
    - macos-x64
    - macos-arm64
    - linux-x64
    - mingw-x64
    - ios-device-arm64
    - ios-simulator-arm64
    - ios-simulator-x64
    - ...

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

processing:

- generate per target bindings:
    - jvm: generate jvm-macosArm64 and jvm-macosX64 and then combine into jvm
    - android: save as in JVM but for abis
    - native/macosArm64 (or other targets): generate native-macosArm64 (...)
- start commonization per sourceSet from more specific
    - jvmCommon: commonize jvm and android
    - macos: commonize macosArm64 and macosX64 (same for iOS)
    - apple: commonize macos and ios
    - native: commonize apple, linux and mingw
    - desktop: commonize macos, linux, mingw and jvm
    - common: commonize everything
- prepare per sourceset declarations
