package com.jgerontis.android.foodie

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "RecipeListFragment"

class RecipeListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onRecipeSelected(recipeId: UUID)
    }

    private var callbacks: Callbacks? = null

    var sortedByCategory = false

    private lateinit var menu: Menu
    private lateinit var recipeRecyclerView: RecyclerView
    val categories = arrayOf("Breakfast","Lunch","Dinner","Desert","Snack")
    private var adapter: RecipeAdapter = RecipeAdapter(emptyList())
    private val recipeListViewModel: RecipeListViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        recipeRecyclerView =
            view.findViewById(R.id.recipe_recycler_view) as RecyclerView
        recipeRecyclerView.layoutManager = LinearLayoutManager(context)
        recipeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeListViewModel.recipeListLiveData.observe(
            viewLifecycleOwner,
            Observer { recipes ->
                recipes?.let {
                    Log.i(TAG, "Got recipes ${recipes.size}")
                    updateUI(recipes)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_recipe_list, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_recipe -> {
                val recipe = Recipe()
                recipeListViewModel.addRecipe(recipe)
                callbacks?.onRecipeSelected(recipe.id)
                true
            }
            R.id.toggle_favorites -> {

                // toggle whether we only see favorites
                recipeListViewModel.toggleFavorites()

                // update the UI
                recipeListViewModel.recipeListLiveData.observe(
                    viewLifecycleOwner,
                    Observer { recipes ->
                        recipes?.let {
                            Log.i(TAG, "Got recipes ${recipes.size}")
                            updateUI(recipes)
                        }
                    })
                true
            }

            R.id.sort_by_category -> {
                // toggle whether we sort by category or date
                if (!sortedByCategory) {
                    recipeListViewModel.sortByCategory()
                } else {
                    recipeListViewModel.sortByDate()
                }
                sortedByCategory = !sortedByCategory

                // update the UI
                recipeListViewModel.recipeListLiveData.observe(
                        viewLifecycleOwner,
                        Observer { recipes ->
                            recipes?.let {
                                Log.i(TAG, "Got recipes ${recipes.size}")
                                updateUI(recipes)
                            }
                        })
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(recipes: List<Recipe>) {
        adapter = RecipeAdapter(recipes)
        recipeRecyclerView.adapter = adapter
    }

    private inner class RecipeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var recipe: Recipe

        private val titleTextView: TextView = itemView.findViewById(R.id.recipe_name)
        private val categoryTextView: TextView = itemView.findViewById(R.id.recipe_category)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.recipe_favorite)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            titleTextView.text = this.recipe.name
            categoryTextView.text = categories[this.recipe.category]
            favoriteImageView.visibility = if (recipe.isFavorite) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${recipe.name} clicked!", Toast.LENGTH_SHORT)
                .show()
            callbacks?.onRecipeSelected(recipe.id)
        }
    }

    private inner class RecipeAdapter(var recipes: List<Recipe>)
        : RecyclerView.Adapter<RecipeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RecipeHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false)
            return RecipeHolder(view)
        }

        override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
            val recipe = recipes[position]
            holder.bind(recipe)
        }

        override fun getItemCount() = recipes.size
    }
}