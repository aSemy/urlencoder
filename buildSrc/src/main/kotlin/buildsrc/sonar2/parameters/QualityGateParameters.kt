package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import javax.inject.Inject

abstract class QualityGateParameters : ExtensionAware {
  /**
   * `sonar.qualitygate.wait`
   *
   * Forces the analysis step to poll the SonarQube instance and wait for the Quality Gate status.
   * If there are no other options, you can use this to fail a pipeline build when the Quality Gate is failing. See the CI integration page for more information. */
  @get:Input
  @get:Optional
  abstract val wait: Property<Boolean> //by container.string("sonar.qualitygate.wait")

  /**
   * `sonar.qualitygate.timeout`
   *
   * Sets the number of seconds that the scanner should wait for a report to be processed.                                                              300 */
  @get:Input
  @get:Optional
  abstract val timeout: Property<Int> //by container.int("sonar.qualitygate.timeout")
}
