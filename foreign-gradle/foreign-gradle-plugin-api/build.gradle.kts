import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
}

kotlin {
    explicitApi()
    jvmToolchain(8)
    compilerOptions {
        // for compatibility with Gradle 8+
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

dependencies {
    compileOnly(gradleApi())

    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)
}

// kotlin:
//   1 target    has N compilations, has N sourceSets
//   1 sourceSet has 1 compilation

kotlin {
    sourceSets {
        val commonMain by getting {
            foreign {

            }
            dependencies {
                api(foreign("common"))
                compileOnly(foreign("common"))
            }
        }
    }
}

dependencies {
    foreign(foreign.conan("openssl")) {

    }
}

foreignToolchains {
    llvm {
        ...
    }
    emscripten {
        ...
    }
}

// package can provide: link and include paths
foreignDependencies {
    val openssl = conan("openssl") {
        // config per CxTarget
    }

    brew("openssl") {
        // openssl@3
    }

    raw("openssl") {
        // config per ForeignTarget
        macosArm64 {

        }
        targets.configureEach {

        }
    }

    cmake("openssl") {

    }

    system("system") {

    }
}

foreignInterfaces {
    cx("libcrypto") {
        // TODO: how to better configure it
        // it will be injected in this compilation tree (main, integrationTest, test, etc)
        compilationTree.set("main")
        packageName.set("")

        jvm {
            runtimeKind = JNI
            macosArm64()
        }
        js {
            nodejs()
        }
        native {
            macosArm64()
        }
        wasm {
            nodejs()
        }

        bindings {
            // optIn, public, etc
            // initialHeaders, header filters, etc

            macosArm64 {

            }
        }
    }
}
