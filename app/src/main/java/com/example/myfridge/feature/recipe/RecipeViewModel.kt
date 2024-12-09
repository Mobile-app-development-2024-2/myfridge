package com.example.myfridge.feature.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfridge.feature.model.Recipe
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

class RecipeViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-aiplatform.googleapis.com/") // Vertex AI API의 기본 URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(GeminiApiService::class.java)

    // StateFlow로 변경하여 collect를 사용하도록 수정
    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> get() = _recipeList

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> get() = _result

    // Fetch recipes by calling the generateText function
    fun fetchRecipes() {
        generateText(
            apiKey = "AIzaSyCt7jWP5g1hswB8qe1RML7tYJjej2dsTj4",
            prompt = "재료를 기반으로 레시피를 생성합니다.",
            model = "Gemini-1.5-flash-002"
        )

        // StateFlow에서 collect를 사용하여 결과를 처리
        viewModelScope.launch {
            result.collect { response ->
                val recipes = parseRecipes(response ?: "")
                _recipeList.value = recipes
            }
        }
    }

    // Function to generate text using Vertex AI
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

    // Function to parse the generated response into a list of recipes
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

// Define the API service interface
interface GeminiApiService {
    @POST("v1/languages:generateText")
    suspend fun generateText(@Body requestBody: RequestBody): ResponseBody
}
