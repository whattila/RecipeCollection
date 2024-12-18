package hu.bme.aut.recipecollection.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.recipecollection.R
import hu.bme.aut.recipecollection.data.Recipe

class RecipeAdapter(private val listener: OnRecipeClickedListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private val items = mutableListOf<Recipe>()
    var timeSum: Int = 0

    interface OnRecipeClickedListener {
        fun onRecipeSelected(item: Recipe?)
        fun onRecipeDeleted(item: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = items[position]

        holder.nameTextView.text = item.name
        holder.categoryTextView.text = item.category.toString()
        holder.removeButton.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.item = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Recipe) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(Recipes: List<Recipe>) {
        items.clear()
        items.addAll(Recipes)
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int) {
        listener.onRecipeDeleted(items[position])
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toBeMadeCheckBox: CheckBox
        val nameTextView: TextView
        val categoryTextView: TextView
        val removeButton: ImageButton

        var item: Recipe? = null

        init {
            toBeMadeCheckBox = itemView.findViewById(R.id.ToBeMadeCheckBox)
            nameTextView = itemView.findViewById(R.id.RecipeNameTextView)
            categoryTextView = itemView.findViewById(R.id.RecipeCategoryTextView)
            removeButton = itemView.findViewById(R.id.RecipeRemoveButton)
            itemView.setOnClickListener { listener.onRecipeSelected(item) }
            toBeMadeCheckBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                run {
                    if (isChecked)
                        timeSum += item?.preparationTimeInMinutes ?: 0
                    else
                        timeSum -= item?.preparationTimeInMinutes ?: 0
                    Log.d("RecipeAdapter", timeSum.toString())
                }
            })
        }
    }
}