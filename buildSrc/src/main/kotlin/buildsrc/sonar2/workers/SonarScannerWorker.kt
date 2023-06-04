package buildsrc.sonar2.workers

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.sonarsource.scanner.api.EmbeddedScanner
import org.sonarsource.scanner.api.LogOutput
import java.time.Duration

abstract class SonarScannerWorker : WorkAction<SonarScannerWorker.Parameters> {

    interface Parameters : WorkParameters {
        val properties: MapProperty<String, String>
        val version: Property<String>
    }

    override fun execute() {
        val properties = parameters.properties.get()
        val version = parameters.version.get()

        val scanner = EmbeddedScanner
            .create("SonarQubedWorker", version, SonarLogger)
            .addGlobalProperties(properties)

        val duration = measureTime {
            scanner.start()
            scanner.execute(emptyMap())
        }

        logger.info("SonarScannerWorker completed in $duration")
    }

    private object SonarLogger : LogOutput {
        override fun log(formattedMessage: String, level: LogOutput.Level) {
            when (level) {
                LogOutput.Level.TRACE -> logger.trace(formattedMessage)
                LogOutput.Level.DEBUG -> logger.debug(formattedMessage)
                LogOutput.Level.INFO  -> logger.info(formattedMessage)
                LogOutput.Level.WARN  -> logger.warn(formattedMessage)
                LogOutput.Level.ERROR -> logger.error(formattedMessage)
            }
        }
    }

    companion object {
        private val logger: Logger = Logging.getLogger(SonarScannerWorker::class.java)

        // can't use kotlin.Duration or kotlin.time.measureTime {} because
        // the implementation isn't stable across Kotlin versions
        private fun measureTime(block: () -> Unit): Duration =
            System.nanoTime().let { startTime ->
                block()
                Duration.ofNanos(System.nanoTime() - startTime)
            }
    }
}
