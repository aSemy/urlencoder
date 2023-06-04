package buildsrc.sonar2.parameters

import buildsrc.internal.adding
import buildsrc.sonar2.parameters.AnalysisParametersMapBuilder.Companion.buildParametersMap
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.kotlin.dsl.*
import javax.inject.Inject

/**
 * See https://docs.sonarqube.org/latest/analyzing-source-code/analysis-parameters/
 */
abstract class AnalysisParameters @Inject constructor(
    private val objects: ObjectFactory,

    private val inputs: TaskInputs,
    private val outputs: TaskOutputs,
) : ExtensionAware {

    fun sonar(key: String, value: String? = null): Property<String> = add<String>("sonar.$key").convention(value)
    fun sonar(key: String, value: Int? = null): Property<Int> = add<Int>("sonar.$key").convention(value)
    fun sonar(key: String, value: Boolean? = null): Property<Boolean> = add<Boolean>("sonar.$key").convention(value)

    private inline fun <reified T> add(
        key: String,
        property: Property<T> = objects.property<T>()
    ): Property<T> {
        extensions.add(key, property)
        inputs.property(key, property)
        return property
    }

    /** `sonar.organization` */
    @get:Input
    @get:Optional
    abstract val organization: Property<String> // by container.string("sonar.projectBaseDir")

    /**
     * `sonar.host.url`
     *
     * the server URL
     * http://localhost:9000 */
    @get:Input
    @get:Optional
    abstract val hostUrl: Property<String> // by container.string("sonar.host.url")

    /** `sonar.verbose` */
    @get:Input
    @get:Optional
    abstract val verbose: Property<Boolean> // by container.boolean("sonar.verbose")

    /**
     * `sonar.token`
     *
     * The authentication token of a SonarQube user with either Execute Analysis permission on the project or Global Execute Analysis permission. */
    @get:Input
    @get:Optional
    abstract val token: Property<String> // by container.string("sonar.token")

    /**
     * `sonar.ws.timeout`
     *
     * Maximum time to wait for the response of a Web Service call (in seconds). Modifying this value from the default is useful only when you're experiencing timeouts during analysis while waiting for the server to respond to Web Service calls.                                                              60 */
    @get:Input
    @get:Optional
    abstract val wsTimeout: Property<Int> // by container.int("sonar.ws.timeout")

    /**
     * `sonar.sources`
     *
     * Comma-separated paths to directories containing main source files.
     * Read from build system for Maven, Gradle, MSBuild projects. Defaults to project base directory when neither sonar.sources nor sonar.tests is provided. */
    @get:InputFiles
    @get:Optional
    @get:PathSensitive(RELATIVE)
    abstract val sources: ConfigurableFileCollection // by container.files("sonar.sources")

    /**
     * `sonar.tests`
     *
     * Comma-separated paths to directories containing test source files.
     * Read from build system for Maven, Gradle, MSBuild projects. Else default to empty. */
    @get:InputFiles
    @get:Optional
    @get:PathSensitive(RELATIVE)
    abstract val tests: ConfigurableFileCollection // by container.files("sonar.tests")

    /**
     * `sonar.sourceEncoding`
     *
     * Encoding of the source files. Ex: UTF-8, MacRoman, Shift_JIS. This property can be replaced by the standard property project.build.sourceEncoding in Maven projects. The list of available encodings depends on your JVM.
     * System encoding */
    @get:Input
    @get:Optional
    abstract val sourceEncoding: Property<String> // by container.string("sonar.sourceEncoding")

    /**
     * `sonar.externalIssuesReportPaths`
     *
     * Comma-delimited list of paths to Generic Issue reports. */
    @get:Input
    @get:Optional
    abstract val externalIssuesReportPaths: ListProperty<String>

    /**
     * `sonar.working.directory`
     *
     * Set the working directory for an analysis triggered with the SonarScanner or the SonarScanner for Ant (versions greater than 2.0). This property is not compatible with the SonarScanner for MSBuild. The path must be relative, and unique for each project.  Beware: the specified folder is deleted before each analysis.                                                              .scannerwork */
    @get:LocalState
    abstract val workingDirectory: DirectoryProperty

    /** sonar.buildString */
    @get:Input
    @get:Optional
    abstract val buildString: Property<String> // by container.string("sonar.buildString")

    /**
     * `sonar.analysis.$yourKey`
     *
     * This property stub allows you to insert custom key/value pairs into the analysis context, which will also be passed forward to webhooks. */
    @get:Input
    @get:Optional
    abstract val analysis: MapProperty<String, String>

    /**
     * `sonar.newCode.referenceBranch`
     *
     * Sets the new code definition to Reference Branch for this analysis, overriding the configuration on the server.
     * The New Code will be calculated based on the differences between the branch under analysis and the provided branch.
     * This parameter is intended to be set in a configuration file (ex: sonar-project.properties), specific to a given branch. */
    @get:Input
    @get:Optional
    abstract val newCodeReferenceBranch: Property<String> // by container.string("sonar.newCode.referenceBranch")

    /**
     * `sonar.filesize.limit`
     *
     * Sets the limit in MB for files to be discarded from the analysis scope if the size is greater than specified.
     * 20 */
    @get:Input
    @get:Optional
    abstract val filesizeLimit: Property<Int> // by container.int("sonar.filesize.limit")

    /**
     * `sonar.projectKey`
     *
     * The project's unique key. Allowed characters are: letters, numbers, -, _, . and :, with at least one non-digit.
     * For Maven projects, this defaults to `<groupId>:<artifactId>` */
    @get:Input
    @get:Optional
    abstract val projectKey: Property<String> // by container.string("sonar.projectKey")

    /**
     * `sonar.projectName`
     *
     * Name of the project that will be displayed on the web interface.
     *
     * <name> for Maven projects, otherwise project key. If not provided and there is already a name in the DB, it won't be overwritten. */
    @get:Input
    @get:Optional
    abstract val projectName: Property<String> // by container.string("sonar.projectName")

    /**
     * `sonar.projectVersion`
     *
     * The project version.
     * <version> for Maven projects, otherwise "not provided". Do not use your build number as sonar.projectVersion. */
    @get:Input
    @get:Optional
    abstract val projectVersion: Property<String> // by container.string("sonar.projectVersion")

    /**
     * `sonar.projectDescription`
     *
     * The project description.
     * <description> for Maven projects */
    @get:Input
    @get:Optional
    abstract val projectDescription: Property<String> // by container.string("sonar.projectDescription")

    /** `sonar.projectDate` */
    @get:Input
    @get:Optional
    abstract val projectDate: Property<String> // by container.string("sonar.projectDate")

    /** `sonar.projectBaseDir` */
    @get:Input
    @get:Optional
    abstract val projectBaseDir: Property<String> // by container.string("sonar.projectBaseDir")

    @get:Nested
    val scm: SourceCodeManagementParameters = extensions.create("scm")

    @get:Nested
    val links: LinksParameters = extensions.create("links")

    @get:Nested
    val qualityGate: QualityGateParameters = extensions.create("qualityGate")

    @get:Nested
    val logging: LoggingParameters = extensions.create("logging")

    @get:Nested
    val scanner: ScannerParameters = extensions.create("scanner")

    @get:Nested
    val android: AndroidParameters = extensions.create("android")

    @get:Nested
    val java: JavaParameters = extensions.create("java")

    @get:Nested
    val kotlin: KotlinParameters = extensions.create("kotlin")

    /**
     * Code duplication settings, for a specific language.
     */
    @get:Nested
    val duplications: NamedDomainObjectContainer<CodeDuplicationParameters> =
        extensions.adding("duplications", objects.domainObjectContainer(CodeDuplicationParameters::class) { name ->
            objects.newInstance<CodeDuplicationParameters>(name)
        })

    internal fun buildMap(): Map<String, String> {
        return buildParametersMap {
            sonar("organization", organization)
            sonar("host.url", hostUrl)
            sonar("token", token)
            sonar("ws.timeout", wsTimeout)
            sonar("sources", sources.elements)
            sonar("tests", tests.elements)
            sonar("sourceEncoding", sourceEncoding)
            sonar("externalIssuesReportPaths", externalIssuesReportPaths)
            sonar("working.directory", workingDirectory)
            sonar("newCode.referenceBranch", newCodeReferenceBranch)
            sonar("filesize.limit", filesizeLimit)
            sonar("buildString", buildString)

            sonar("projectKey", projectKey)
            sonar("projectName", projectName)
            sonar("projectVersion", projectVersion)
            sonar("projectDate", projectDate)
            sonar("projectDescription", projectDescription)

            sonar("scm.provider", scm.provider)
            sonar("scm.forceReloadAll", scm.forceReloadAll)
            sonar("scm.exclusions.disabled", scm.exclusionsDisabled)
            sonar("scm.revision", scm.revision)

            sonar("links.homepage", links.homepage)
            sonar("links.ci", links.ci)
            sonar("links.issue", links.issue)
            sonar("links.scm", links.scm)

            sonar("qualitygate.wait", qualityGate.wait)
            sonar("qualitygate.timeout", qualityGate.timeout)

            sonar("log.level", logging.level)
            sonar("verbose", verbose)

            sonar("scanner.dumpToFile", scanner.dumpToFile)
            sonar("scanner.metadataFilePath", scanner.metadataFile)

            sonar("android.detected", android.detected)
            sonar("android.minsdkversion.min", android.minSdk.min)
            sonar("android.minsdkversion.max", android.minSdk.max)

            sonar("java.binaries", java.binaries.elements)
            sonar("java.libraries", java.libraries.elements)
            sonar("java.test.binaries", java.testBinaries.elements)
            sonar("java.test.libraries", java.testLibraries.elements)

            sonar("kotlin.source.version", kotlin.sourceVersion)
            sonar("kotlin.threads", kotlin.threads)
            sonar("kotlin.skipUnchanged", kotlin.skipUnchanged)

            analysis.orNull?.forEach { (key, value) ->
                sonar("analysis.$key", value)
            }

            duplications.forEach { cpd ->
                val language = cpd.language

                sonar("cpd.${language}.minimumTokens", cpd.minimumTokens)
                sonar("cpd.${language}.minimumLines", cpd.minimumLines)
            }
        }
    }
}
