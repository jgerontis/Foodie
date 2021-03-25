package com.jgerontis.android.foodie

import android.app.Application

class FoodieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RecipeRepository.initialize(this)
    }
}