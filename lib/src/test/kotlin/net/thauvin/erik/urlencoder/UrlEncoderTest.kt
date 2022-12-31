/*
 * Copyright 2001-2022 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Copyright 2022 Erik C. Thauvin (erik@thauvin.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.thauvin.erik.urlencoder

import net.thauvin.erik.urlencoder.UrlEncoder.decode
import net.thauvin.erik.urlencoder.UrlEncoder.encode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue


class UrlEncoderTest {
    private val invalid = arrayOf("sdkjfh%", "sdkjfh%6", "sdkjfh%xx", "sdfjfh%-1")
    private val same = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789-_.~"
    private val validMap = mapOf(
        "a test &" to "a%20test%20%26",
        "!abcdefghijklmnopqrstuvwxyz%%ABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789-_.~=" to
                "%21abcdefghijklmnopqrstuvwxyz%25%25ABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789-_.~%3D",
        "%#okékÉȢ smile!😁" to "%25%23ok%C3%A9k%C3%89%C8%A2%20smile%21%F0%9F%98%81"
    )

    @Test
    fun testDecode() {
        assertEquals("", decode(""))
        assertSame(same, decode(same))
        validMap.forEach {
            assertEquals(it.key, decode(it.value))
        }
        invalid.forEach {
            assertFailsWith(IllegalArgumentException::class) {
                decode(it)
            }
        }
    }

    @Test
    fun testEncode() {
        assertEquals("", encode(""))
        assertSame(same, encode(same))
        assertSame(same, encode(same, ""))
        assertTrue(encode("").isEmpty())
        validMap.forEach {
            assertEquals(it.value, encode(it.key))
        }
        assertEquals("?test=a%20test", encode("?test=a test", '=', '?'))
        assertEquals("?test=a%20test", encode("?test=a test", "=?"))
        assertEquals("aaa", encode("aaa", 'a'))
    }
}
