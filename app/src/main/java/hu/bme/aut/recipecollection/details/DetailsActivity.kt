package hu.bme.aut.recipecollection.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.recipecollection.R
import hu.bme.aut.recipecollection.data.Recipe
import android.app.SearchManager
import android.content.Intent
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), SearchDialogFragment.SearchDialogListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val recipe = intent.getSerializableExtra(EXTRA_RECIPE) as? Recipe
            ?: throw RuntimeException("Extra must be a valid Recipe object!")
        initView(recipe)

        btnSearch.setOnClickListener {
            val dialog = SearchDialogFragment()
            dialog.searchText = tvIngredients.text.toString()
            dialog.show(
                supportFragmentManager,
                SearchDialogFragment.TAG
            )
        }
    }

    private fun initView(recipe: Recipe?) {
        if (recipe != null) {
            supportActionBar?.title = recipe.category.toString()
            tvCategory.text = recipe.name
            tvIngredients.text = recipe.ingredients
            tvPreparationTime.text = "${recipe.preparationTimeInMinutes} minutes"
            tvPreparation.text = recipe.preparation
        }
        else
            throw RuntimeException("The Recipe object cannot be null!")
    }

    override fun onSearch(searchText: String) {
        val intentSearch = Intent(Intent.ACTION_WEB_SEARCH)
        intentSearch.putExtra(SearchManager.QUERY, "buying " + searchText)
        startActivity(intentSearch)
    }

    companion object {
        private const val TAG = "DetailsActivity"
        const val EXTRA_RECIPE = "extra_recipe"
    }
}