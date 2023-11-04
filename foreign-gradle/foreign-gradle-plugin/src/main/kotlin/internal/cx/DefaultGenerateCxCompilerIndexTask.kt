package dev.whyoleg.foreign.gradle.internal.cx

//internal abstract class DefaultGenerateCxCompilerIndexTask @Inject constructor(
//    objectFactory: ObjectFactory
//) : GenerateCxCompilerIndexTask, DefaultTask() {
//    override val destinationDirectory: DirectoryProperty = objectFactory.directoryProperty()
//    override val target: Property<ForeignTarget> = objectFactory.property()
//    override val dependencies: MapProperty<ForeignDependency, FileSystemLocation> = objectFactory.mapProperty()
//
//    @get:InputFiles
//    val toolClasspath: ConfigurableFileCollection = objectFactory.fileCollection()
//
//    @get:Inject
//    abstract val workerExecutor: WorkerExecutor
//
//    @TaskAction
//    fun generate() {
//        workerExecutor.classLoaderIsolation {
//            it.classpath.from(toolClasspath)
//        }.submit(GenerateCxCompilerIndexAction::class) {
//            // TODO: other properties
//            it.target.set(target)
//            it.dependencies.set(dependencies)
//        }
//    }
//}
//
//// only it can access ForeignGradleTool
//internal interface GenerateCxCompilerIndexAction : WorkAction<GenerateCxCompilerIndexAction.Parameters> {
//    interface Parameters : WorkParameters {
//        val inputFile: RegularFileProperty
//        val outputFile: RegularFileProperty
//        val target: Property<ForeignTarget>
//        val dependencies: MapProperty<ForeignDependency, FileSystemLocation>
//        val additionalCompilerArguments: ListProperty<String>
//    }
//
//    override fun execute(): Unit = with(parameters) {
//        ForeignGradleTool.buildIndex(
//            inputFile = inputFile.get().asFile,
//            outputFile = outputFile.get().asFile,
//            target = target.get(),
//            dependencies = dependencies.get().mapValues { it.value.asFile },
//            additionalCompilerArguments = additionalCompilerArguments.get()
//        )
//    }
//}
