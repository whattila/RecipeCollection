package hu.bme.aut.recipecollection.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.recipecollection.R
import hu.bme.aut.recipecollection.data.Recipe
import hu.bme.aut.recipecollection.data.RecipeDatabase
import hu.bme.aut.recipecollection.details.DetailsActivity
import hu.bme.aut.recipecollection.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlin.concurrent.thread

class ListActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickedListener, NewRecipeDialogFragment.NewRecipeDialogListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var database: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            NewRecipeDialogFragment().show(
                supportFragmentManager,
                NewRecipeDialogFragment.TAG
            )
        }

        btnCountdown.setOnClickListener {
            if (adapter.timeSum <= 0) {
                Snackbar.make(btnCountdown, R.string.warn_message, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val countdownIntent = Intent()
            countdownIntent.setClass(this, TimerActivity::class.java)
            countdownIntent.putExtra(TimerActivity.EXTRA_TIME, adapter.timeSum)
            startActivity(countdownIntent)
        }

        database = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            "recipe-collection"
        ).build()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = RecipeAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_list, menu)
        for (i in 0 until menu.size()) {
            val menuItem: MenuItem = menu.getItem(i)
            menuItem.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
            if (menuItem.hasSubMenu()) {
                val subMenu: SubMenu = menuItem.subMenu
                for (j in 0 until subMenu.size()) {
                    subMenu.getItem(j)
                        .setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.filter_all -> {
                loadItemsInBackground()
                item.isChecked = true
                true
            }
            R.id.filter_starter -> {
                loadFilteredItems(Recipe.Category.STARTER)
                item.isChecked = true
                true
            }
            R.id.filter_soup -> {
                loadFilteredItems(Recipe.Category.SOUP)
                item.isChecked = true
                true
            }
            R.id.filter_maincourse -> {
                loadFilteredItems(Recipe.Category.MAINCOURSE)
                item.isChecked = true
                true
            }
            R.id.filter_dessert -> {
                loadFilteredItems(Recipe.Category.DESSERT)
                item.isChecked = true
                true
            }
            R.id.filter_drink -> {
                loadFilteredItems(Recipe.Category.DRINK)
                item.isChecked = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.recipeDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun loadFilteredItems(category: Recipe.Category){
        thread {
            val items = database.recipeDao().getSpecificRecipes(category)
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onRecipeCreated(newItem: Recipe) {
        thread {
            val newId = database.recipeDao().insert(newItem)
            val newRecipe = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newRecipe)
            }
        }
    }

    override fun onRecipeSelected(item: Recipe?) {
        if (item != null) {
            val showDetailsIntent = Intent()
            showDetailsIntent.setClass(this, DetailsActivity::class.java)
            showDetailsIntent.putExtra(DetailsActivity.EXTRA_RECIPE, item)
            startActivity(showDetailsIntent)
        }
    }

    override fun onRecipeDeleted(item: Recipe) {
        thread {
            database.recipeDao().deleteItem(item)
        }
    }
}