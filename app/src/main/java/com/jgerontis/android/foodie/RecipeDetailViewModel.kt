package com.jgerontis.android.foodie

import androidx.lifecycle.*
import java.util.*

class RecipeDetailViewModel() : ViewModel() {

    private val recipeRepository = RecipeRepository.get()
    private val recipeIdLiveData = MutableLiveData<UUID>()

    var recipeLiveData: LiveData<Recipe?> =
        Transformations.switchMap(recipeIdLiveData) { recipeId ->
            recipeRepository.getRecipe(recipeId)
        }

    fun loadRecipe(recipeId: UUID) {
        recipeIdLiveData.value = recipeId
    }

    fun getCategory(id : UUID) : LiveData<Int> {
        return recipeRepository.getCategory(id)
    }

    fun saveRecipe(recipe: Recipe) {
        recipeRepository.updateRecipe(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        recipeRepository.deleteRecipe(recipe)
    }
}