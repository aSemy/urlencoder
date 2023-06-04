package buildsrc.sonar2.parameters

import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.create

abstract class AndroidParameters : ExtensionAware {
    /**
     * `sonar.android.detected`
     *
     * This property will be set when android project is being analyzed
     */
    @get:Input
    @get:Optional
    abstract val detected: Property<Boolean> //by container.boolean("sonar.android.detected")

    @get:Nested
    val minSdk: MinSdk = extensions.create("minSdk")

    abstract class MinSdk : ExtensionAware {

        /**
         * `sonar.android.minsdkversion.min`
         *
         * This property contains the minimum value of `minSdkVersion` properties defined in build.gradle.
         * In case we detect the specific variant of the project being analyzed, then we set the value of minSdkVersion defined for this variant
         *
         * Not every android project needs to set this property.
         */
        @get:Input
        @get:Optional
        abstract val min: Property<String> //by container.string("sonar.android.minsdkversion.min")

        /**
         * `sonar.android.minsdkversion.max`
         *
         * This property contains the maximum value of minSdkVersion properties defined in build.gradle.
         * In case we detect the specific variant of the project being analyzed, then we set the value of minSdkVersion defined for this variant
         *
         * Not every android project needs to set this property.
         */
        @get:Input
        @get:Optional
        abstract val max: Property<String> //by container.string("sonar.android.minsdkversion.max")
    }
}
