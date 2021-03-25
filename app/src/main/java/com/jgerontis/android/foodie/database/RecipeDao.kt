package com.jgerontis.android.foodie.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jgerontis.android.foodie.Recipe
import java.util.*

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY date DESC")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe ORDER BY category DESC")
    fun getRecipesByCategory(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1 ORDER BY date DESC")
    fun getRecipesFavorite(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE isFavorite = 1 ORDER BY category DESC")
    fun getRecipesFavoriteByCategory(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id=(:id)")
    fun getRecipe(id: UUID): LiveData<Recipe?>

    @Query("SELECT category FROM recipe where id=(:id)")
    fun getCategory(id: UUID): LiveData<Int>

    @Update
    fun updateRecipe(recipe: Recipe)

    @Insert
    fun addRecipe(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)
}