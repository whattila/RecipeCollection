package hu.bme.aut.recipecollection.list

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.recipecollection.R
import hu.bme.aut.recipecollection.data.Recipe

class NewRecipeDialogFragment : DialogFragment() {
    private lateinit var nameEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var ingredientsEditText: EditText
    private lateinit var preparationTimeEditText: EditText
    private lateinit var preparationEditText: EditText

    interface NewRecipeDialogListener {
        fun onRecipeCreated(newItem: Recipe)
    }

    private lateinit var listener: NewRecipeDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewRecipeDialogListener
            ?: throw RuntimeException("Activity must implement the NewRecipeDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_recipe)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { dialogInterface, i ->
                if (isValid())
                    listener.onRecipeCreated(getRecipe())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_recipe, null)

        nameEditText = contentView.findViewById(R.id.RecipeNameEditText)
        categorySpinner = contentView.findViewById(R.id.RecipeCategorySpinner)
        categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.category_items)
            )
        )
        ingredientsEditText = contentView.findViewById(R.id.IngredientsEditText)
        preparationTimeEditText = contentView.findViewById(R.id.PreparationTimeEditText)
        preparationEditText = contentView.findViewById(R.id.PreparationEditText)

        return contentView
    }

    private fun isValid() = (nameEditText.text.isNotEmpty() && ingredientsEditText.text.isNotEmpty() && preparationTimeEditText.text.isNotEmpty() && preparationEditText.text.isNotEmpty())

    private fun getRecipe() = Recipe(
        id = null,
        name = nameEditText.text.toString(),
        category = Recipe.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: Recipe.Category.MAINCOURSE,
        ingredients = ingredientsEditText.text.toString(),
        preparationTimeInMinutes = try {
            preparationTimeEditText.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            0
        },
        preparation = preparationEditText.text.toString()
    )

    companion object {
        const val TAG = "NewRecipeDialogFragment"
    }
}