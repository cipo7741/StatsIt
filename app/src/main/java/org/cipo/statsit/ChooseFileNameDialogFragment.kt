package org.cipo.statsit

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.DialogFragment

class ChooseFileNameDialogFragment : DialogFragment() {


    // Use this instance of the interface to deliver action events
    internal lateinit var listener: NoticeDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val edittext = EditText(activity)
        edittext.inputType = InputType.TYPE_CLASS_TEXT

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Please enter file name:")
                .setView(edittext)
                .setPositiveButton("OK"
                ) { _, _ ->
                    // FIRE ZE MISSILES!
                    listener.onDialogPositiveClick(edittext.text.toString())

                }
                .setNegativeButton("Cancel"
                ) { _, _ ->
                    Toast.makeText(activity,"Nothing happened!",Toast.LENGTH_SHORT).show()
                    // User cancelled the dialog
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: String)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }


}
