/*
 * Copyright 2001-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package buildsrc.utils

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Rife2TestListener(
    private val testBadgeApiKey: Provider<String>
) : TestListener {
    override fun beforeTest(p0: TestDescriptor?) = Unit
    override fun beforeSuite(p0: TestDescriptor?) = Unit
    override fun afterTest(desc: TestDescriptor, result: TestResult) = Unit
    override fun afterSuite(desc: TestDescriptor, result: TestResult) {
        if (desc.parent == null) {
            val passed = result.successfulTestCount
            val failed = result.failedTestCount
            val skipped = result.skippedTestCount

            val apiKey = testBadgeApiKey.orNull

            if (apiKey != null) {
                println(apiKey)
                val response: HttpResponse<String> = HttpClient.newHttpClient()
                    .send(
                        HttpRequest.newBuilder()
                            .uri(
                                URI(
                                    "https://rife2.com/tests-badge/update/net.thauvin.erik/urlencoder?" +
                                            "apiKey=$apiKey&" +
                                            "passed=$passed&" +
                                            "failed=$failed&" +
                                            "skipped=$skipped"
                                )
                            )
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build(), HttpResponse.BodyHandlers.ofString()
                    )
                println("RESPONSE: ${response.statusCode()}")
                println(response.body())
            }
        }
    }
}
