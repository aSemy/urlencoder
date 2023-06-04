package buildsrc.sonar2.parameters

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import javax.inject.Inject

abstract class ScannerParameters @Inject constructor(
//    private val root: AnalysisParameters
//    private val objects: ObjectFactory
) : ExtensionAware {
    /**
     * `sonar.scanner.dumpToFile`
     *
     * Outputs to the specified file the full list of properties passed to the scanner API as a means to debug analysis. */
    @get:Internal
    abstract val dumpToFile: RegularFileProperty // by container.file("sonar.scanner.dumpToFile", output = true)

    /**
     * `sonar.scanner.metadataFilePath`
     *
     * Set the location where the scanner writes the report-task.txt file containing among other things the `ceTaskId`. */
    @get:Internal
    abstract val metadataFile: RegularFileProperty //by container.file("sonar.scanner.metadataFilePath", output = true)
}
