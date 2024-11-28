package dev.whyoleg.foreign.gradle.worker

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.tasks.*
import org.gradle.workers.*
import javax.inject.*

public abstract class ForeignWorkerTask : DefaultTask() {
    @get:Classpath
    public abstract val workerClasspath: ConfigurableFileCollection

    @get:Inject
    public abstract val workerExecutor: WorkerExecutor

    protected inline fun <reified A : WorkAction<T>, T : WorkParameters> workerExecute(configure: Action<T>) {
        workerExecutor.processIsolation {
            it.classpath.from(workerClasspath)
        }.submit(A::class.java, configure)
    }
}

public fun Project.configureWorkerClasspath(task: TaskProvider<ForeignWorkerTask>) {
    val classpath = workerClasspath()
    task.configure {
        it.workerClasspath.from(classpath)
    }
}

private fun Project.workerClasspath(): FileCollection {
    configurations.findByName(FOREIGN_WORKER_CLASSPATH_RESOLVER_CONFIGURATION)?.let { return it }

    val configuration = configurations.create(FOREIGN_WORKER_CLASSPATH_CONFIGURATION) {
        it.isCanBeResolved = false
        it.isCanBeConsumed = false
        it.isVisible = false
    }

    val configurationResolver = configurations.create(FOREIGN_WORKER_CLASSPATH_RESOLVER_CONFIGURATION) {
        it.isCanBeResolved = true
        it.isCanBeConsumed = false
        it.isVisible = false

        it.extendsFrom(configuration)
    }

    dependencies.add(
        FOREIGN_WORKER_CLASSPATH_CONFIGURATION,
        "dev.whyoleg.foreign:foreign-gradle-worker-classpath:${WorkerConstants.VERSION}"
    )

    return configurationResolver
}

private const val FOREIGN_WORKER_CLASSPATH_CONFIGURATION = "foreign-worker-classpath"
private const val FOREIGN_WORKER_CLASSPATH_RESOLVER_CONFIGURATION = "foreign-worker-resolver-classpath"
