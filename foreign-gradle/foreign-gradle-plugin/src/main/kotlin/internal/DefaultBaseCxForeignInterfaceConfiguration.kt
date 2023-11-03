package dev.whyoleg.foreign.gradle.internal

//internal abstract class DefaultLeafCxInterop(
//    objectFactory: ObjectFactory,
//    parent: DefaultBaseCxInterop?
//) : DefaultBaseCxInterop(objectFactory, parent) {
//    final override val bindings: DefaultForeignCxInterfaceConfigurationBindings =
//        DefaultLeafForeignCxInterfaceConfigurationBindings(objectFactory, parent?.bindings)
//}
//
//internal abstract class DefaultBaseCxInterop(
//    objectFactory: ObjectFactory,
//    parent: DefaultBaseCxInterop?
//) : BaseCxInterop {
//    final override val includeDirectories: ListProperty<Directory> =
//        objectFactory.listProperty<Directory>().withAllFrom(parent?.includeDirectories)
//    final override val libraryDirectories: ListProperty<Directory> =
//        objectFactory.listProperty<Directory>().withAllFrom(parent?.libraryDirectories)
//    final override val libraryLinkageNames: ListProperty<String> =
//        objectFactory.listProperty<String>().withAllFrom(parent?.libraryLinkageNames)
//    abstract override val bindings: DefaultForeignCxInterfaceConfigurationBindings
//}
