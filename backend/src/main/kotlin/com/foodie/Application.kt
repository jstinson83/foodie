package com.foodie

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// Pass in our interface as a parameter, defaulting to the real Gemini implementation
fun Application.module(
    recipeParser: RecipeParser = GeminiRecipeParser(
        HttpClient(CIO) {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        },
        System.getenv("GEMINI_API_KEY") ?: ""
    )
) {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Foodie Backend is running! Ready to receive recipes.")
        }

        post("/recipe/parse") {
            val multipart = call.receiveMultipart()
            var imageBytes: ByteArray? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    imageBytes = part.streamProvider().readBytes()
                }
                part.dispose()
            }

            if (imageBytes == null) {
                call.respond(HttpStatusCode.BadRequest, "No image uploaded")
                return@post
            }

            try {
                // Pass the raw bytes to our clean parser interface
                val recipe = recipeParser.parseRecipeImage(imageBytes!!)
                call.respond(recipe)
            } catch (e: IllegalArgumentException) {
                // This catches the missing API key error thrown by the parser constructor/method
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Configuration error")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadGateway, "Error parsing recipe: ${e.message}")
            }
        }
    }
}
