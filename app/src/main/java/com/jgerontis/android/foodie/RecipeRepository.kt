package com.jgerontis.android.foodie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.jgerontis.android.foodie.database.RecipeDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "recipe-database"

class RecipeRepository private constructor(context: Context) {

    private val database : RecipeDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecipeDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val recipeDao = database.recipeDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getRecipesFavorite(): LiveData<List<Recipe>> = recipeDao.getRecipesFavorite()

    fun getRecipes(): LiveData<List<Recipe>> = recipeDao.getRecipes()

    fun getRecipesByCategory(): LiveData<List<Recipe>> = recipeDao.getRecipesByCategory()

    fun getRecipesFavoriteByCategory(): LiveData<List<Recipe>> = recipeDao.getRecipesFavoriteByCategory()

    fun getRecipe(id: UUID): LiveData<Recipe?> = recipeDao.getRecipe(id)

    fun getCategory(id: UUID): LiveData<Int> = recipeDao.getCategory(id)

    fun updateRecipe(recipe: Recipe) {
        executor.execute {
            recipeDao.updateRecipe(recipe)
        }
    }

    fun addRecipe(recipe: Recipe) {
        executor.execute {
            recipeDao.addRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        executor.execute {
            recipeDao.deleteRecipe(recipe)
        }
    }

    companion object {
        private var INSTANCE: RecipeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = RecipeRepository(context)
            }
        }

        fun get(): RecipeRepository {
            return INSTANCE ?:
            throw IllegalStateException("RecipeRepository must be initialized")
        }
    }
}