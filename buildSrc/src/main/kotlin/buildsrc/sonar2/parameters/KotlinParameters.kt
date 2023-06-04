package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class KotlinParameters : ExtensionAware {

    /**
     * `sonar.kotlin.source.version`
     *
     * You can explicitly define which Kotlin version the analyzer should analyze your code based on.
     * Provide the desired version in the format `X.Y` as value to the
     * `sonar.kotlin.source.version` property (e.g. `1.7`).
     */
    @get:Input
    @get:Optional
    abstract val sourceVersion: Property<String> //by container.string("sonar.kotlin.source.version")

    /**
     * `sonar.kotlin.threads`
     *
     * As of August 2022, the Kotlin analyzer supports multithreaded parsing.
     * This is an experimental feature and is disabled by default.
     * You can enable it by providing an appropriate number of threads with the property key `sonar.kotlin.threads`.
     * */
    @get:Input
    @get:Optional
    abstract val threads: Property<Int> //by container.int("sonar.kotlin.threads")

    /**
     * `sonar.kotlin.skipUnchanged`
     *
     * Starting from November 2022, and by default, the Kotlin analyzer optimizes the analysis of unchanged files in
     * pull requests. In practice, this means that the analyzer does not perform an analysis on any file that is the
     * same as on the PR's target branch. As long as the project is configured in such a way that the analyzer is able
     * to find the project's binaries, this should not impact the analysis results.
     *
     * If you wish to disable this optimization, you can set the value of the analysis parameter
     * `sonar.kotlin.skipUnchanged` to `false`.
     * Leaving the parameter unset lets the server decide whether the optimization should be enabled.
     */
    @get:Input
    @get:Optional
    abstract val skipUnchanged: Property<Boolean> //by container.boolean("sonar.kotlin.skipUnchanged")

}
