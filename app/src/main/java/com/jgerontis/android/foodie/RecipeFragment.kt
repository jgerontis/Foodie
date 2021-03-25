package com.jgerontis.android.foodie

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val TAG = "RecipeFragment"
private const val ARG_RECIPE_ID = "recipe_id"

class RecipeFragment : Fragment() {

    private lateinit var recipe: Recipe
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var favoriteCheckBox: CheckBox
    private lateinit var directionsField: EditText
    private lateinit var deleteButton: Button
    private lateinit var spinner : Spinner
    val categories = arrayOf("Breakfast","Lunch","Dinner","Desert","Snack")
    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeDetailViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = Recipe()
        val recipeId: UUID = arguments?.getSerializable(ARG_RECIPE_ID) as UUID
        recipeDetailViewModel.loadRecipe(recipeId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)

        titleField = view.findViewById(R.id.recipe_name) as EditText
        dateButton = view.findViewById(R.id.recipe_date) as Button
        favoriteCheckBox = view.findViewById(R.id.recipe_favorite) as CheckBox
        directionsField = view.findViewById(R.id.recipe_directions) as EditText
        deleteButton = view.findViewById(R.id.delete_button) as Button

        dateButton.apply {
            text = recipe.date.toString()
            isEnabled = false
        }


        // var category = recipeDetailViewModel.getCategory(recipeId)

        deleteButton.setOnClickListener {
            recipeDetailViewModel.deleteRecipe(recipe)
            Toast.makeText(view.context, "Deleted! Press back.", Toast.LENGTH_SHORT).show()
        }

        spinner = view.findViewById<Spinner>(R.id.spinner) as Spinner
        if (spinner != null) {
            // Initializing a String Array

            // Initializing an ArrayAdapter
            val adapter = ArrayAdapter(
                view.context, // Context
                android.R.layout.simple_spinner_item, // Layout
                categories // Array
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            spinner.adapter = adapter
            // Toast.makeText(view.context, category.toString() + recipe.category.toString(), Toast.LENGTH_SHORT).show()
            spinner.setSelection(recipe.category, false)


        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeDetailViewModel.recipeLiveData.observe(
            viewLifecycleOwner,
            Observer { recipe ->
                recipe?.let {
                    this.recipe = recipe
                    updateUI()
                }
            }
        )

    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.name = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        favoriteCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                recipe.isFavorite = isChecked
            }
        }

        val directionsWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.directions = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        directionsField.addTextChangedListener(directionsWatcher)


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                recipe.category = position
                recipeDetailViewModel.saveRecipe(recipe)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    override fun onStop() {
        super.onStop()
        recipeDetailViewModel.saveRecipe(recipe)
    }

    private fun updateUI() {
        titleField.setText(recipe.name)
        dateButton.text = recipe.date.toString()
        favoriteCheckBox. apply {
            isChecked = recipe.isFavorite
            jumpDrawablesToCurrentState()
        }
        directionsField.setText(recipe.directions)
        spinner.setSelection(recipe.category, false)
    }


    companion object {

        fun newInstance(crimeId: UUID): RecipeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_RECIPE_ID, crimeId)
            }
            return RecipeFragment().apply {
                arguments = args
            }
        }
    }
}


