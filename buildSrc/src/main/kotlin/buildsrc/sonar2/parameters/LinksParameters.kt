package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class LinksParameters : ExtensionAware {
    /**
     * `sonar.links.homepage`
     *
     * Project home page.
     * <url> for Maven projects */
    @get:Input
    @get:Optional
    abstract val homepage: Property<String> // by container.string("sonar.links.homepage")

    /**
     * `sonar.links.ci`
     *
     * Continuous integration.
     * <ciManagement><url> for Maven projects */
    @get:Input
    @get:Optional
    abstract val ci: Property<String> // by container.string("sonar.links.ci")

    /**
     * `sonar.links.issue`
     *
     * Issue tracker.
     * <issueManagement><url> for Maven projects */
    @get:Input
    @get:Optional
    abstract val issue: Property<String> // by container.string("sonar.links.issue")

    /**
     * `sonar.links.scm`
     *
     * Project source repository.
     * <scm><url> for Maven projects */
    @get:Input
    @get:Optional
    abstract val scm: Property<String> // by container.string("sonar.links.scm")
}
