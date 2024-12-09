package com.example.myfridge.feature.recipe
import com.example.myfridge.feature.model.Recipe
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myfridge.R
import com.example.myfridge.feature.food.FoodViewModel
import com.example.myfridge.ui.theme.MintWhite
import com.example.myfridge.ui.theme.fontMint



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(navController: NavController) {

    val foodViewModel: FoodViewModel = hiltViewModel()
    val recipeViewModel: RecipeViewModel = hiltViewModel()

    // Fetch food list from FoodViewModel
    val foodList by foodViewModel.foodList.collectAsState()

    // Extract ingredients (names of the food items)
    val ingredient1 = foodList.getOrNull(0)?.name ?: ""  // Use the first food item as ingredient1
    val ingredient2 = foodList.getOrNull(1)?.name ?: ""  // Use the second food item as ingredient2
    val recipeCount = 3 // Example count, you can set this dynamically

    // Fetch recipes when the food list changes
    LaunchedEffect(key1 = foodList) {
        if (ingredient1.isNotEmpty() && ingredient2.isNotEmpty()) {
            println("Ingredients to fetch: $ingredient1, $ingredient2") // 디버깅 로그
            recipeViewModel.fetchRecipes(ingredient1, ingredient2, recipeCount)
        }
    }

    val recipeList by recipeViewModel.recipeList.collectAsState()
    val selectedRecipe by recipeViewModel.selectedRecipe.collectAsState() // 선택된 레시피 텍스트 상태
    val isLoading by recipeViewModel.isLoading.collectAsState() // 로딩 상태 관찰

    // Check if recipeList is updated
    println("Recipe List: $recipeList") // 디버깅 로그

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MintWhite,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                title = {
                    Text(
                        text = "레시피 목록",
                        color = fontMint,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MintWhite)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // 로딩 중이면 로딩 UI 표시
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                // 선택된 레시피 텍스트 표시
                selectedRecipe?.let {
                    Text(
                        text = it.description, // 상세 텍스트 표시
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                }

                // LazyColumn for displaying recipes
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Display recipes, if no recipes exist, show sample data
                    val displayedRecipes = if (recipeList.isEmpty()) {
                        // Show sample data if no recipes available
                        listOf(
                            Recipe("Scrambled Eggs", "A simple recipe for scrambled eggs."),
                            Recipe("Egg Salad", "A delicious egg salad with various ingredients."),
                            Recipe("Tomato Soup", "A rich tomato soup perfect for a quick meal.")
                        )
                    } else {
                        recipeList
                    }

                    items(displayedRecipes) { recipe ->
                        RecipeCard(recipe = recipe, onClick = {
                            recipeViewModel.selectRecipe(recipe) // 클릭된 레시피 상태 업데이트
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .clickable { onClick() }, // 클릭 시 onClick 호출
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_food),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.description,
                    color = Color.Gray
                )
            }
            Image(
                painter = painterResource(id = R.drawable.pointer),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(8.dp)
            )
        }
    }
}
