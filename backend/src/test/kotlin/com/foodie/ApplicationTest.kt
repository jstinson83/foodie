package com.foodie

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.client.request.forms.*

// A fake parser we inject specifically for tests so they run without the Gemini API Key
class FakeRecipeParser : RecipeParser {
    override suspend fun parseRecipeImage(imageBytes: ByteArray): RecipeResponse {
        return RecipeResponse(
            title = "Grandma's Chocolate Chip Cookies",
            ingredients = listOf(
                Ingredient("2 1/4 cups all-purpose flour", "all-purpose flour", 2.25, "cup")
            ),
            steps = listOf("Bake it")
        )
    }
}

class ApplicationTest {
    @Test
    fun testParseEndpoint() = testApplication {
        application {
            // Inject the fake parser
            module(recipeParser = FakeRecipeParser())
        }
        
        val response = client.submitFormWithBinaryData(
            url = "/recipe/parse",
            formData = formData {
                append("image", byteArrayOf(1, 2, 3), Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"dummy.jpg\"")
                })
            }
        )
        
        assertEquals(HttpStatusCode.OK, response.status)
        
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("Grandma's Chocolate Chip Cookies"), "Title was missing in JSON")
        assertTrue(responseBody.contains("all-purpose flour"), "Ingredient name was missing in JSON")
        assertTrue(responseBody.contains("2.25"), "Parsed quantity math was missing in JSON")
        
        println("=== TEST PASSED ===")
    }
}
