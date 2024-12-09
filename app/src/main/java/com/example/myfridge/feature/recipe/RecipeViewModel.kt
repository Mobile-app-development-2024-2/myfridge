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
        .baseUrl("https://us-central1-aiplatform.googleapis.com/") // Vertex AI API's base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(GeminiApiService::class.java)

    private val _recipeList = MutableStateFlow<List<Recipe>>(emptyList())
    val recipeList: StateFlow<List<Recipe>> get() = _recipeList

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null) // Selected recipe state
    val selectedRecipe: StateFlow<Recipe?> get() = _selectedRecipe

    private val _isLoading = MutableStateFlow<Boolean>(false) // Loading state
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> get() = _result

    // Fetch recipes by calling the generateText function with ingredients as input
    fun fetchRecipes(ingredient1: String, ingredient2: String, recipeCount: Int) {
        val promptTemplate = "%s과 %s를 재료로 하는 요리 %d개를 추천해줘. 총 조리 시간은 최대 30분으로 해. " +
                "레시피 이름을 첫 번째로 제시하고, 두 번째로 재료와 각 재료의 양을 나열해. " +
                "마지막에는 요리 방법을 단계마다 숫자를 붙여서 각 조리 시간과 함께 나열해줘."

        val prompt = promptTemplate.format(ingredient1, ingredient2, recipeCount)
        println("Fetching recipes for ingredients: $ingredient1, $ingredient2") // 디버깅 로그

        // Start loading
        _isLoading.value = true

        generateText(
            apiKey = "AIzaSyCt7jWP5g1hswB8qe1RML7tYJjej2dsTj4", // API Key should be stored securely
            prompt = prompt,
            model = "Gemini-1.5-flash-002"
        )
    }

    private fun generateText(apiKey: String, prompt: String, model: String) {
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
                val responseBody = response.body()?.string()
                println("API Response: $responseBody") // 디버깅 로그
                _result.value = responseBody
                parseRecipes(responseBody) // Parse the response body into recipes
            } catch (e: Exception) {
                println("Error fetching recipes: ${e.message}") // 디버깅 로그
                _result.value = null
                // If error occurs or no response, set sample data
                setSampleData()
            }
        }
    }

    // Function to parse the generated response into a list of recipes
    private fun parseRecipes(response: String?) {
        response?.let {
            val recipeList = mutableListOf<Recipe>()
            val recipes = it.split("\n\n")
            for (recipeText in recipes) {
                val lines = recipeText.split("\n")
                if (lines.size >= 2) {
                    val name = lines[0]
                    val description = lines.subList(1, lines.size).joinToString("\n")
                    recipeList.add(Recipe(name, description))
                }
            }
            if (recipeList.isEmpty()) {
                // If no valid recipes are parsed, set sample data
                setSampleData()
            } else {
                _recipeList.value = recipeList // Update the recipe list state
            }
            _isLoading.value = false // Set loading to false when recipes are loaded
            println("Recipes parsed: $recipeList") // 디버깅 로그
        } ?: run {
            setSampleData() // If response is null, set sample data
        }
    }

    // Set sample data when there is no valid recipe data
    private fun setSampleData() {
        _recipeList.value = listOf(
            Recipe(
                name = "Scrambled Eggs",
                description = "A simple recipe for scrambled eggs with ingredient1 and ingredient2."
            ),
            Recipe(
                name = "Egg Salad",
                description = "A delicious salad made with ingredient1 and ingredient2."
            ),
            Recipe(
                name = "Tomato Soup",
                description = "A rich and creamy tomato soup with ingredient1 and ingredient2."
            )
        )
        _isLoading.value = false // Set loading to false
        println("Sample data set.") // 디버깅 로그
    }

    // Select a recipe
    fun selectRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
    }
}


// Define the API service interface
interface GeminiApiService {
    @POST("v1/languages:generateText")
    suspend fun generateText(@Body requestBody: RequestBody): retrofit2.Response<ResponseBody> // Modify to Response<ResponseBody>
}
