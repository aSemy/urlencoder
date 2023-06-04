package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class LoggingParameters : ExtensionAware {

    /**
     * `sonar.log.level`
     *
     * Control the quantity/level of logs produced during an analysis.
     *
     * * DEBUG: Display INFO logs + more details at DEBUG level. Similar to `sonar.verbose=true`.
     * * TRACE: Display DEBUG logs + the timings of all ElasticSearch queries and Web API calls executed by the SonarScanner.
     * * INFO
     */
    @get:Input
    @get:Optional
    abstract val level: Property<String> //by container.string("sonar.log.level")
}
