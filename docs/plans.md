# Current plans

## 0.1.0

- finish writing codegen for declarations for all targets
- start writing gradle plugin (jvm + native support only, JS/WASM is still super experimental)
    - design and create gradle plugin to build/link/load/distribute FFI
    - design minimal dependency provider API:
        - direct paths (for prebuilt)
        - brew packages (for clang f.e.)
        - conan packages (? useful for samples ?)
- improve codegen for other things
- decide on 'kotlinx.cinterop like runtime' - may be it's not needed? :)
- ??? setup multi-release JAR for FFM ???
    - it's needed for generated declarations. 3 modes should be available:
        - JNI - will use JNI calls
        - FFM - will use Panama FFM calls
        - BOTH - depending on java runtime will use or FFM (JDK 20) or JNI
    - is it needed for runtime artifacts?
- Add support for UNSAFE for jvm
- Add support for cleaner for android
- create several samples (not all of them could be created):
    - openssl (both libcrypto and libssl)
    - git - https://libgit2.org
    - ssh - https://www.libssh2.org
    - ffmpeg - https://www.ffmpeg.org
    - curl - https://curl.se
    - clang - https://clang.llvm.org/doxygen/group__CINDEX.html
    - sqlite - https://www.sqlite.org/index.html
    - tensorflow - https://www.tensorflow.org
    - flatbuffers - https://flatbuffers.dev
    - zeromq - https://zeromq.org
- setup publication:
    - how to embed cx-index-cli K/N binaries?

## TODO

* binding generation should support commonization (similar how cinterop does)
    * expect/actual per platform
    * additional optional expectation annotation for JVM declarations which available only on single OS:
      macos, linux, windows (f.e. for some posix function)

## Notes

* compiler plugin will generate only code needed to work with foreign functions
* gradle plugin will provide configuration for includes, linking arguments and will link code with native library
* additional tool inside gradle plugin will allow to generate bindings for provided headers.
  those bindings will be included as kotlin generated source files and processed by compiler plugin

* foreign-runtime-core - basic work with memory/calls/sizes/etc
* foreign-runtime-c

* foreign-gradle-plugin
* foreign-idea-plugin
* foreign-compiler-plugin - Kotlin compiler plugin
* foreign-clang
    * foreign-clang-core - models to work with libclang. shared between CLI and runner
    * foreign-clang-cli - K/N wrapper over libclang
* foreign-codegen - takes some input like clang models and produces some code

## Compatibility

* foreign-runtime - should be JDK 8 and Android compatible
* foreign-gradle-plugin - Gradle 8+ (Kotlin 1.8) and JDK 17 compatible
* 