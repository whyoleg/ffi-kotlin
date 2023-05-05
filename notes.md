## Gradle plugin

### JNI (no common)

Workflow:

- generate C sources
    - build JNI library
    - put JNI library into resources
- generate Kotlin sources
    - link kotlin sources to sourceSet

TODO:

- decide on `long` on Windows (it's 32bit even on 64bit systems)
- what to do with declarations that are available on JVM per platform, f.e. only on Windows or macOS
- how to distribute JNI library when it's built on different machines:
- it looks like we need to somehow use gradle metadata for per platform builds,
  similar to how kotlin plugin uses it for native targets but more complex

Parameters:

- include paths - for JNI + declaration generation
- link paths - for JNI
- library loading - shared / prebuilt / none
- bindings kind - JNI / FFM / JNI+FFM
- headers to index
- filters for indexing
- visibility/packages, etc
- package for bindings
- name of the produced bindings library (should be unique for package)
- configure for compilation (as in cinterop) or for sourceSet or for target?
- 
