package com.jgerontis.android.foodie

import androidx.lifecycle.ViewModel

class RecipeListViewModel : ViewModel() {

    private val recipeRepository = RecipeRepository.get()
    var recipeListLiveData = recipeRepository.getRecipes()
    var favorites : Boolean = false

    fun addRecipe(recipe: Recipe) {
        recipeRepository.addRecipe(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }

    fun toggleFavorites() {
        favorites = !favorites

        recipeListLiveData = if (favorites) {
            recipeRepository.getRecipesFavorite()
        } else {
            recipeRepository.getRecipes()
        }

    }

    fun sortByCategory() {
        recipeListLiveData = if (favorites) {
            recipeRepository.getRecipesFavoriteByCategory()
        } else {
            recipeRepository.getRecipesByCategory()
        }
    }

    fun sortByDate() {
        recipeListLiveData = if (favorites) {
            recipeRepository.getRecipesFavorite()
        } else {
            recipeRepository.getRecipes()
        }
    }

}