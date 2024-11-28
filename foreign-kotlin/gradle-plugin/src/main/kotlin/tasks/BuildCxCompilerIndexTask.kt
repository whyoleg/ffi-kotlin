package dev.whyoleg.foreign.gradle.tasks

//public interface BuildCxCompilerIndexTask : Task {
//
//    // TODO: Input vs Internal and Provide vs plain for constants - what should be used?
//    @get:Input
//    public val interopName: Provider<String>
//
//    @get:Input
//    public val kotlinPlatform: Provider<KotlinPlatform>
//
//    @get:Input
//    public val nativeTarget: Provider<NativeTarget>
//
//    @get:Input
//    public val initialHeaders: ListProperty<String>
//
//    @get:Input
//    public val includeDirectories: ListProperty<Directory>
//
//    @get:Input
//    public val toolchainDependencies: MapProperty<ToolchainDependency, FileSystemLocation>
//
//    @get:Internal
//    public val destinationDirectory: DirectoryProperty
//
//    @get:OutputFile
//    public val outputIndexFile: Provider<RegularFile>
//
//    @get:OutputFile // TODO - should this be exposed?
//    public val outputIndexDumpFile: Provider<RegularFile>
//}
//
//public interface FilterCxCompilerIndexTask : Task {
//
//    // TODO: Input vs Internal and Provide vs plain for constants - what should be used?
//    @get:Input
//    public val interopName: Provider<String>
//
//    @get:Input
//    public val kotlinPlatform: Provider<KotlinPlatform>
//
//    @get:Input
//    public val nativeTarget: Provider<NativeTarget>
//
//    @get:Input
//    public val includeHeaders: ListProperty<Spec<String>>
//
//    @get:Input
//    public val excludeHeaders: ListProperty<Spec<String>>
//
//    @get:InputFile
//    public val inputIndexFile: RegularFileProperty
//
//    @get:InputFile
//    public val inputIndexDumpFile: RegularFileProperty
//}
