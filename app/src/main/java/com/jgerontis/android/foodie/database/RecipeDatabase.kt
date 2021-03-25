package com.jgerontis.android.foodie.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jgerontis.android.foodie.Recipe

private const val DATABASE_NAME = "recipe-database"

@Database(entities = [ Recipe::class ], version=1)
@TypeConverters(RecipeTypeConverters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}