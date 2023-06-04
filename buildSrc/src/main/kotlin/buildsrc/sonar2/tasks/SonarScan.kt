package buildsrc.sonar2.tasks

import buildsrc.sonar2.parameters.AnalysisParameters
import buildsrc.sonar2.parameters.AnalysisParametersMapBuilder.Companion.buildParametersMap
import buildsrc.sonar2.workers.SonarScannerWorker
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.submit
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.workers.WorkerExecutor
import org.sonarqube.gradle.SonarExtension
import javax.inject.Inject

/**
 * Analyses one or more projects with the [SonarQube Scanner](http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle).
 * Can be used with or without the `"sonar-gradle"` plugin.
 * If used together with the plugin, `properties` will be populated with defaults based on Gradle's object model and user-defined
 * values configured via [SonarExtension].
 * If used without the plugin, all properties have to be configured manually.
 * For more information on how to configure the SonarQube Scanner, and on which properties are available, see the
 * [SonarQube Scanner documentation](http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle).
 */
abstract class SonarScan @Inject constructor(
    private val workers: WorkerExecutor,
) : DefaultTask() {
    /**
     * @return The String key/value pairs to be passed to the SonarQube Scanner.
     * `null` values are not permitted.
     */
    @get:Nested
    abstract val analysisParameters: Property<AnalysisParameters>

    @get:Classpath
    abstract val runtimeClasspath: ConfigurableFileCollection

    @get:Input
    abstract val sonarQubedVersion: Property<String>

    @get:Input
    abstract val gradleVersion: Property<String>

    init {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
    }

    @TaskAction
    fun run() {
        val parametersMap = analysisParameters.get().buildMap()

        logger.lifecycle("parametersMap: $parametersMap")

        val workQueue = workers.classLoaderIsolation {
            classpath.from(runtimeClasspath)
        }

        workQueue.submit(SonarScannerWorker::class) {
            properties.set(parametersMap)
            version.set("${sonarQubedVersion.get()}/${gradleVersion.get()}")
        }
    }
}
