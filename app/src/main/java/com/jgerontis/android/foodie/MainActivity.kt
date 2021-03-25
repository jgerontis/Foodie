package com.jgerontis.android.foodie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    RecipeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = RecipeListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onRecipeSelected(recipeId: UUID) {
        val fragment = RecipeFragment.newInstance(recipeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}