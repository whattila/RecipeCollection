package hu.bme.aut.recipecollection.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(value = [Recipe.Category::class])
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}