package hu.bme.aut.recipecollection.details

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.recipecollection.R

class SearchDialogFragment : DialogFragment()  {
    private lateinit var searchTargetEditText: EditText
    var searchText = ""

    interface SearchDialogListener {
        fun onSearch(searchText: String)
    }

    private lateinit var listener: SearchDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SearchDialogListener
            ?: throw RuntimeException("Activity must implement the SearchDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.search_for))
            .setView(getContentView())
            .setPositiveButton(R.string.search) { dialogInterface, i ->
                listener.onSearch(searchTargetEditText.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_search, null)
        searchTargetEditText = contentView.findViewById(R.id.SearchTargetEditText)
        searchTargetEditText.setText(searchText)
        return contentView
    }

    companion object {
        const val TAG = "SearchDialogFragment"
    }
}