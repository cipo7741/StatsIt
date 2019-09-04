package org.cipo.statsit.calcu_list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.cipo.statsit.R
import org.cipo.statsit.calcu_list.db.decodeIntAsString

class CalculationFragment : Fragment() {

    var clickListener: (String) -> Unit = fun(_: String) {}

    var operation: String = ""
    var result: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the fragment layout
        val rootView = inflater.inflate(R.layout.fragment_calculation, container, false)

        // Get a reference to the TextViews in the fragment layout
        val textViewOperation = rootView.findViewById(R.id.calculation_textViewOperation) as TextView
        val textViewResult = rootView.findViewById(R.id.calculation_textViewResult) as TextView

        textViewOperation.text = operation
        textViewResult.text = decodeIntAsString(result)

        textViewOperation.setOnClickListener{
            clickListener(operation)
        }

        // Return the rootView
        return rootView
    }





}

