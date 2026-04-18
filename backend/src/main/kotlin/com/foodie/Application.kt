package com.foodie

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import io.ktor.http.content.*
import io.ktor.server.request.*
import kotlinx.coroutines.delay

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@Serializable
data class RecipeResponse(
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>
)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Foodie Backend is running! Ready to receive recipes.")
        }

        post("/recipe/parse") {
            // 1. Receive the multipart form data (the photo)
            val multipart = call.receiveMultipart()
            
            // Simulating AI processing delay
            delay(1000)
            
            // 2. Return the dummy contract data for immediate satisfaction
            call.respond(RecipeResponse(
                title = "Grandma's Chocolate Chip Cookies",
                ingredients = listOf(
                    "2 1/4 cups all-purpose flour", 
                    "1 tsp baking soda",
                    "1/2 tsp salt",
                    "1 cup butter, softened"
                ),
                steps = listOf(
                    "Preheat oven to 375 degrees F.",
                    "Combine flour, baking soda and salt in small bowl.",
                    "Beat butter, granulated sugar, brown sugar and vanilla extract in large mixer bowl until creamy.",
                    "Bake for 9 to 11 minutes or until golden brown."
                )
            ))
        }
    }
}
