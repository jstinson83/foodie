package com.foodie

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.client.request.forms.*

class ApplicationTest {
    @Test
    fun testParseEndpoint() = testApplication {
        // Spin up the Ktor application module
        application {
            module()
        }
        
        // Use the Ktor test client to send a mock multipart image upload
        val response = client.submitFormWithBinaryData(
            url = "/recipe/parse",
            formData = formData {
                append("image", byteArrayOf(1, 2, 3), Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"dummy.jpg\"")
                })
            }
        )
        
        // Assert the endpoint succeeded
        assertEquals(HttpStatusCode.OK, response.status)
        
        // Assert the JSON payload matches the expected contract
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("Grandma's Chocolate Chip Cookies"), "Title was missing in JSON")
        assertTrue(responseBody.contains("2 1/4 cups all-purpose flour"), "Ingredients were missing in JSON")
        
        println("=== TEST PASSED ===")
        println("Received JSON payload from backend:")
        println(responseBody)
    }
}
