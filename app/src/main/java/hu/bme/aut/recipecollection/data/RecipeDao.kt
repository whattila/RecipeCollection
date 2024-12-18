package hu.bme.aut.recipecollection.data

import androidx.room.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE category = :category")
    fun getSpecificRecipes(category: Recipe.Category): List<Recipe>

    @Insert
    fun insert(recipes: Recipe): Long

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun deleteItem(recipe: Recipe)
}