package buildsrc.sonar2.parameters

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Optional

abstract class JavaParameters : ExtensionAware {


    /** (required) Comma-separated paths to directories containing the compiled bytecode files corresponding to your source files. */
    @get:Classpath
    @get:Optional
    abstract val binaries: ConfigurableFileCollection //by container.string("sonar.java.binaries")

    /** Comma-separated paths to files with third-party libraries (JAR or Zip files) used by your project.*/
    @get:Classpath
    @get:Optional
    abstract val libraries: ConfigurableFileCollection //by container.string("sonar.java.libraries")

    /** Comma-separated paths to directories containing the compiled bytecode files corresponding to your test files */
    @get:Classpath
    @get:Optional
    abstract val testBinaries: ConfigurableFileCollection //by container.string("sonar.java.test.binaries")

    /** Comma-separated paths to files with third-party libraries (JAR or Zip files) used by your tests.
     *
     * For example, this should include the junit jar)
     */
    @get:Classpath
    @get:Optional
    abstract val testLibraries: ConfigurableFileCollection //by container.files("sonar.java.test.libraries")

}
