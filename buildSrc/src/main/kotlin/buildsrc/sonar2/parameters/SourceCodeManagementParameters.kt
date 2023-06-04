package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional


abstract class SourceCodeManagementParameters : ExtensionAware {

    /**
     * `sonar.scm.provider`
     *
     * This property can be used to explicitly tell SonarQube which SCM you're using on the project
     * (in case auto-detection doesn't work). The value of this property is always lowercase and depends on the
     * SCM (ex. "git" if you're using Git). Check the SCM integration documentation for more. */
    @get:Input
    @get:Optional
    abstract val provider: Property<String> //by container.string("sonar.scm.provider")

    /**
     * `sonar.scm.forceReloadAll`
     *
     * By default, blame information is only retrieved for changed files.
     *
     * Set this property to `true` to load blame information for all files. This can be useful if you feel that some SCM data is outdated but SonarQube does not get the latest information from the SCM engine. */
    @get:Input
    @get:Optional
    abstract val forceReloadAll: Property<Boolean> //by container.boolean("sonar.scm.forceReloadAll")

    /**
     * `sonar.sonar.scm.disabled`
     */
    @get:Input
    @get:Optional
    abstract val disabled: Property<Boolean>

    /**
     * `sonar.scm.exclusions.disabled`
     *
     * For supported engines, files ignored by the SCM, i.e. files listed in `.gitignore`, will automatically be
     * ignored by analysis too.
     * Set this property to `true` to disable that feature.
     * SCM exclusions are always disabled if `sonar.scm.disabled` is set to true. */
    @get:Input
    @get:Optional
    abstract val exclusionsDisabled: Property<Boolean> //by container.boolean("sonar.scm.exclusions.disabled")

    /**
     * `sonar.scm.revision`
     *
     * Overrides the revision, for instance, the Git SHA-1, displayed in analysis results.
     * By default value is provided by the CI environment or guessed by the checked-out sources. */
    @get:Input
    @get:Optional
    abstract val revision: Property<String> //by container.string("sonar.scm.revision")
}
