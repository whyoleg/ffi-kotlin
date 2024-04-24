package dev.whyoleg.foreign.gradle.plugin.dsl.cinterface

import dev.whyoleg.foreign.gradle.plugin.dsl.*
import org.gradle.api.*

// wasm target can be only one, but it could be using different memories - for it's just stubs
public interface ForeignWasmCInterface : ForeignPlatformCInterface, ForeignWasmBaseCInterface {
    override val targets: NamedDomainObjectContainer<ForeignWasmTargetCInterface>
}

public interface ForeignWasmTargetCInterface : ForeignTargetCInterface, ForeignWasmBaseCInterface {
    override val foreignTarget: ForeignTarget.Wasm
}

public interface ForeignWasmBaseCInterface : ForeignBaseCInterface {
    //embedStaticLibraries
    //embedLinkPaths
}
