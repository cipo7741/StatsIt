package org.cipo.statsit.calcu_list.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import org.cipo.statsit.R


class ChoosePercentageDialogFragment(private val percent: Int) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val percentagePickerView = LayoutInflater.from(context).inflate(R.layout.percent_picker, null, false)
            val percentagePickerTens = percentagePickerView.findViewById<NumberPicker>(R.id.tens_picker)
            percentagePickerTens.maxValue = 9
            percentagePickerTens.minValue = 0
            percentagePickerTens.value = percent / 10
            val percentagePickerOnes = percentagePickerView.findViewById<NumberPicker>(R.id.ones_picker)
            percentagePickerOnes.maxValue = 9
            percentagePickerOnes.minValue = 0
            percentagePickerOnes.value = percent % 10

            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_choose_percentage)
                .setView(percentagePickerView)
                .setPositiveButton(R.string.dialog_yes
                ) { _, _ ->
                    val listener = activity as ChoosenPercentageListener?
                    listener?.onFinishingNumberDialog(
                        percentagePickerView.findViewById<NumberPicker>(R.id.tens_picker).value,
                        percentagePickerView.findViewById<NumberPicker>(R.id.ones_picker).value)
                    dismiss()
                }
                .setNegativeButton(R.string.dialog_no
                ) { _, _ ->
                    Toast.makeText(activity, getString(R.string.nothing_happend), Toast.LENGTH_LONG).show()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    interface ChoosenPercentageListener {
        fun onFinishingNumberDialog(tens: Int, ones: Int)
    }
}