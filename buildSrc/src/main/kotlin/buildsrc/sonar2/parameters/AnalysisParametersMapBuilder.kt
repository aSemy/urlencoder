package buildsrc.sonar2.parameters

import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider

/**
 * Convert [AnalysisParameters] to a `Map<String, String>`.
 */
internal class AnalysisParametersMapBuilder(
    private val content: MutableMap<String, String> = mutableMapOf(),
) : Map<String, String> by content {

    fun sonar(prop: String, value: String?) {
        if (value != null) content["sonar.$prop"] = value
    }

    @JvmName("sonarStringProvider")
    fun sonar(prop: String, value: Provider<String>): Unit =
        sonar(prop, value.orNull)

    @JvmName("sonarIntProvider")
    fun sonar(prop: String, value: Provider<Int>): Unit =
        sonar(prop, value.map(Int::toString))

    @JvmName("sonarBooleanProvider")
    fun sonar(prop: String, value: Provider<Boolean>): Unit =
        sonar(prop, value.map(Boolean::toString))

    @JvmName("sonarStringsProvider")
    fun sonar(prop: String, value: Provider<List<String>>): Unit =
        sonar(prop, value.map(List<String>::joinToString))

    @JvmName("sonarFileProvider")
    fun sonar(prop: String, value: Provider<out FileSystemLocation>): Unit =
        sonar(prop, value.orNull?.asFile?.invariantSeparatorsPath)

    @JvmName("sonarFilesProvider")
    fun sonar(prop: String, value: Provider<out Collection<FileSystemLocation>>): Unit =
        sonar(prop, value.map { files -> files.map { it.asFile.invariantSeparatorsPath } })

    fun <T> sonar(prop: String, value: Collection<T>, transform: (T) -> String): Unit =
        sonar(prop, value.joinToString(transform = transform))

    override fun toString(): String = content.toString()

    companion object {
        internal inline fun buildParametersMap(
            build: AnalysisParametersMapBuilder.() -> Unit
        ): Map<String, String> = AnalysisParametersMapBuilder().apply(build)
    }
}
