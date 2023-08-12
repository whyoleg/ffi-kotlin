plugins {
    alias(libs.plugins.build.parameters)
}

buildParameters {
    bool("ci") {
        fromEnvironment()
        defaultValue.set(false)
    }
    group("kotlin") {
        group("override") {
            string("version") {
                description.set("Override Kotlin version")
            }
        }
    }
    group("skip") {
        bool("test") {
            description.set("Skip running tests")
            defaultValue.set(false)
        }
        bool("link") {
            description.set("Skip linking native tests")
            defaultValue.set(false)
        }
    }
}
