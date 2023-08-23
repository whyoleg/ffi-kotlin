plugins {
    id("org.gradlex.build-parameters") version "1.4.3"
}

group = "foreignbuild"

buildParameters {
    enableValidation.set(false)

    bool("ci") {
        fromEnvironment()
        defaultValue.set(false)
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
