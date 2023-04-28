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
- provide 2 runtimes: whole-new and kx.cinterop-like
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
