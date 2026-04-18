package com.foodie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.util.Base64

interface RecipeParser {
    suspend fun parseRecipeImage(imageBytes: ByteArray): RecipeResponse
}

class GeminiRecipeParser(private val httpClient: HttpClient, private val apiKey: String) : RecipeParser {
    override suspend fun parseRecipeImage(imageBytes: ByteArray): RecipeResponse {
        if (apiKey.isBlank()) {
            throw IllegalArgumentException("GEMINI_API_KEY environment variable is not configured.")
        }
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        
        val prompt = """
            Extract the recipe from this image. Return pure JSON matching this exact schema:
            {
              "title": "string",
              "ingredients": [
                {
                  "rawText": "string",
                  "name": "string",
                  "quantity": number or null,
                  "unit": "string or null"
                }
              ],
              "steps": ["string"]
            }
        """.trimIndent()

        val requestBody = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64Image))
                    )
                )
            ),
            generationConfig = GenerationConfig()
        )

        val response: HttpResponse = httpClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent") {
            url { parameters.append("key", apiKey) }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        if (response.status == HttpStatusCode.OK) {
            val geminiResponse = response.body<GeminiResponse>()
            val jsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
            
            return Json { ignoreUnknownKeys = true }.decodeFromString<RecipeResponse>(jsonString)
        } else {
            throw Exception("Gemini API error: ${response.bodyAsText()}")
        }
    }
}
