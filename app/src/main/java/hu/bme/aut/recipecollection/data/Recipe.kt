package hu.bme.aut.recipecollection.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

@Entity(tableName = "recipe")
data class Recipe(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "preparation_time_in_minutes") val preparationTimeInMinutes: Int,
    @ColumnInfo(name = "preparation") val preparation: String
) : Serializable {
    enum class Category {
        STARTER, SOUP, MAINCOURSE, DESSERT, DRINK;

        override fun toString(): String = when (this) {
            STARTER -> "Starter"
            SOUP -> "Soup"
            MAINCOURSE -> "Main course"
            DESSERT -> "Dessert"
            DRINK -> "Drink"
        }

        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Category? {
                var ret: Category? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: Category): Int {
                return category.ordinal
            }
        }
    }
}