package buildsrc.sonar2.parameters

import org.gradle.api.Named
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import javax.inject.Inject

abstract class CodeDuplicationParameters @Inject constructor(
    @get:Input
    val language: String,
) : Named {

    /**
     * `sonar.cpd.${language}.minimumTokens`
     *
     * A piece of code is considered duplicated as soon as there are at least 100 duplicated tokens in a row
     * (override with `sonar.cpd.${language}.minimumTokens`) spread across at least 10 lines of code
     * (override with `sonar.cpd.${language}.minimumLines)`.
     *
     * For Java projects, a piece of code is considered duplicated when there is a series of at least 10
     * statements in a row, regardless of the number of tokens and lines. This threshold cannot be overridden.
     *
     * Default: 100 */
    @get:Input
    @get:Optional
    abstract val minimumTokens: Property<Int>

    /**
     * `sonar.cpd.${language}.minimumLines`
     * @see minimumTokens
     */
    @get:Input
    @get:Optional
    abstract val minimumLines: Property<Int>

    @Internal // tracked by `name`
    override fun getName(): String = language
}
