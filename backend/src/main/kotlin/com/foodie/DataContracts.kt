package com.foodie

import kotlinx.serialization.Serializable

// --- Our App Domain Models ---

@Serializable
data class Ingredient(
    val rawText: String,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null
)

@Serializable
data class RecipeResponse(
    val title: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>
)

// --- Gemini API Models ---

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String? = null,
    val inlineData: InlineData? = null
)

@Serializable
data class InlineData(
    val mimeType: String,
    val data: String
)

@Serializable
data class GenerationConfig(
    val responseMimeType: String = "application/json"
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)
