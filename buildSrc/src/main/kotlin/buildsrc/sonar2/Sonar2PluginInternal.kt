/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Copyright 2022-2023 Erik C. Thauvin (erik@thauvin.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package buildsrc.sonar2

import buildsrc.sonar2.parameters.AnalysisParameters
import buildsrc.sonar2.tasks.SonarScan
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.internal.provider.PropertyInternal
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.internal.state.ModelObject
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.newInstance
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import java.io.File
import javax.inject.Inject

abstract class Sonar2PluginInternal @Inject constructor(
    private val objects: ObjectFactory,
    private val providers: ProviderFactory,
) : Plugin<Project> {

    override fun apply(target: Project) {

        val sonarTask = target.tasks.create<SonarScan>("sonar")

        val analysisParameters: AnalysisParameters = objects.newInstance(
            sonarTask.inputs,
            sonarTask.outputs,
        )


        // https://github.com/gradle/gradle/issues/6619
//        (analysisParameters.scanner.dumpToFile as PropertyInternal<*>).attachProducer(sonarTask as ModelObject)
//        (analysisParameters.scanner.metadataFile as PropertyInternal<*>).attachProducer(sonarTask as ModelObject)


        val sonarExtension: Sonar2Extension = createSonarExtension(
            project = target,
            analysisParameters = analysisParameters,
        )

        val sonarScannerRuntimeClasspath = target.configurations.register("sonarScanner") {
            description = "Runtime classpath for the SonarQube scanner"
            isCanBeResolved = true
            isCanBeConsumed = false
            isVisible = false

            attributes {
                attribute(USAGE_ATTRIBUTE, objects.named(JAVA_RUNTIME))
            }

            withDependencies {
                addLater(
                    sonarExtension.sonarScannerVersion.map { v ->
                        target.dependencies.create("org.sonarsource.scanner.api:sonar-scanner-api:$v")
                    }
                )
            }
        }

        sonarTask.apply {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            runtimeClasspath.from(sonarScannerRuntimeClasspath.map {
                it.incoming.artifacts.artifactFiles
            })
            sonarQubedVersion.convention(sonarExtension.sonarScannerVersion)
            gradleVersion.convention(providers.provider { project.gradle.gradleVersion })
            this.analysisParameters.set(analysisParameters)

            outputs.file(sonarExtension.parameters.scanner.dumpToFile).optional()
            outputs.file(sonarExtension.parameters.scanner.metadataFile).optional()
        }
    }

    private fun createSonarExtension(
        project: Project,
        analysisParameters: AnalysisParameters,
    ): Sonar2Extension {

        val extension = project.extensions.create<Sonar2Extension>("sonar", analysisParameters).apply {
            sonarScannerVersion.convention("2.16.2.588")

            parameters.projectName.convention { project.name }
            parameters.projectDescription.convention { project.description }
            parameters.projectVersion.convention { project.version.toString() }
            parameters.projectBaseDir.convention { project.rootDir.invariantSeparatorsPath }
            parameters.workingDirectory.convention(project.layout.buildDirectory.dir("sonar"))

            parameters.sourceEncoding.convention("UTF-8")
        }

        project.pluginManager.apply {
            withPlugin("org.jetbrains.kotlin.android") { configureKotlin(project, extension.parameters) }
            withPlugin("org.jetbrains.kotlin.js") { configureKotlin(project, extension.parameters) }
            withPlugin("org.jetbrains.kotlin.jvm") { configureKotlin(project, extension.parameters) }
            withPlugin("org.jetbrains.kotlin.multiplatform") { configureKotlin(project, extension.parameters) }
        }

        return extension
    }

    private fun configureKotlin(project: Project, sonar: AnalysisParameters) {

        val kotlin: KotlinProjectExtension = project.extensions.getByType()

        fun sourceFiles(suffix: String) = kotlin.sourceSets
            .filter { it.name.endsWith(suffix, ignoreCase = true) }
            .flatMap { it.kotlin.srcDirs }
            .filter(File::exists)

        sonar.sources.from(sourceFiles("main"))
        sonar.tests.from(sourceFiles("test"))

    }


    private inline fun <reified T> Property<T>.convention(crossinline value: () -> T?) {
        convention(providers.provider { value() })
    }

}
