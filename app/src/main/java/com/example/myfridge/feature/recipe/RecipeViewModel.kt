package com.example.myfridge.feature.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfridge.feature.model.Recipe
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor() : ViewModel() {
    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> get() = _recipeList

    private val location = "us-central1"
    private val projectId = "" // Add your project ID
    private val baseUrl =
        "https://$location-aiplatform.googleapis.com/v1/projects/$projectId/locations/$location/publishers/google/models:generateContent"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val service = retrofit.create(GeminiApiService::class.java)

    fun fetchRecipes() {
        generateText(
            apiKey = "your-api-key",
            prompt = "재료를 기반으로 레시피를 생성합니다.",
            model = "Gemini-1.5-flash-002"
        )

        viewModelScope.launch {
            result.collect { response ->
                val recipes = parseRecipes(response ?: "")
                _recipeList.value = recipes
            }
        }
    }

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> get() = _result

    fun generateText(apiKey: String, prompt: String, model: String) {
        viewModelScope.launch {
            val requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                """
              {
                "model": "$model",
                "prompt": "$prompt",
                "apiKey": "$apiKey"
              }
            """.trimIndent()
            )

            try {
                val response = service.generateText(requestBody)
                _result.value = response.string()
            } catch (e: Exception) {
                _result.value = null
            }
        }
    }

    private fun parseRecipes(response: String): List<Recipe> {
        return try {
            val recipeList = mutableListOf<Recipe>()
            val recipes = response.split("\n\n")
            for (recipeText in recipes) {
                val lines = recipeText.split("\n")
                if (lines.size >= 2) {
                    val name = lines[0]
                    val description = lines.subList(1, lines.size).joinToString("\n")
                    recipeList.add(Recipe(name, description))
                }
            }
            recipeList
        } catch (e: Exception) {
            emptyList()
        }
    }
}

interface GeminiApiService {
    @POST("v1/languages:generateText")
    suspend fun generateText(@Body requestBody: RequestBody): ResponseBody
}
