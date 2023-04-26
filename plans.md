# Current plans

## 0.1.0

- finish writing codegen for declarations for all targets
- start writing gradle plugin
    - design and create gradle plugin to build/link/load/distribute FFI
    - design minimal dependency provider API:
        - direct paths (for prebuilt)
        - brew packages (for clang f.e.)
        - conan packages (? useful for samples ?)
- improve codegen for other things
- provide 2 runtimes: whole-new and kx.cinterop-like
- decide on how to distribute JNI vs FFM APIs
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
