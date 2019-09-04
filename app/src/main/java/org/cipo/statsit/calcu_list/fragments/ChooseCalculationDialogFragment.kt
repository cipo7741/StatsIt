package org.cipo.statsit.calcu_list.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import org.cipo.statsit.R


class ChooseCalculationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_operation)
                .setIcon(R.drawable.ic_check_box_white_24dp)
                .setItems(R.array.operations
                ) { _, which ->
                    val listener = activity as ChoosenOperationListener?
                    listener!!.onFinishingListDialog(which)
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    interface ChoosenOperationListener {
        fun onFinishingListDialog(choice: Int)
    }
}
