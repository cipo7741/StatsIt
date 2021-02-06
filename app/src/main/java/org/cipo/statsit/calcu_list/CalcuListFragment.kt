package org.cipo.statsit.calcu_list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.cipo.statsit.FileList
import org.cipo.statsit.R
import org.cipo.statsit.calcu_list.db.Entry
import org.cipo.statsit.calcu_list.db.encodeDoubleStringAsInt
import org.cipo.statsit.calcu_list.fragments.CalculationFragment
import org.cipo.statsit.calcu_list.fragments.ChooseCalculationDialogFragment
import org.cipo.statsit.calcu_list.fragments.ChoosePercentageDialogFragment
import org.cipo.statsit.calcu_list.fragments.SaveFileDialogFragment
import java.io.File
import java.io.FileOutputStream

class CalcuListFragment : Fragment(),
    ChooseCalculationDialogFragment.ChoosenOperationListener,
    ChoosePercentageDialogFragment.ChoosenPercentageListener,
    SaveFileDialogFragment.NoticeDialogListener {

    private lateinit var entryViewModel: CalcuListAllViewModel

    private lateinit var editTextValue: EditText

    private var currentOperation = "Total"

    private var currentPercent = 0

    private var calculations: MutableMap<String, Long> = mutableMapOf(
        "Total" to 0L,
        "Min" to 0L,
        "Max" to 0L,
        "Mean" to 0L,
        "Count" to 0L
    )

    private var getCountSelected: Int = 0

    private val fragManager = parentFragmentManager

    private var item: FileList.FileItem? = null

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_calcu_list)
//
//        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
//        setSupportActionBar(toolbar)

        arguments?.let {
            if (it.containsKey(ARG_FILE_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = FileList(context!!.filesDir).filesMap[it.getString(ARG_FILE_ID)!!]
                // TODO: show list name
//                activity?.toolbar_calcu_list?.title = item?.name
            }
        }
    }

    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.calcu_list, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_calcu_list)

        val adapter = container?.context?.let {
            CalcuListAdapter(it) { entry: Entry ->
                itemClicked(
                    entry,
                    recyclerView.context,
                    rootView
                )
            }
        }
        recyclerView.adapter = adapter

//    recyclerView.adapter = item?.let {
//            fileItem ->
//
//        val fileContent = FileContent(fileItem)
//        if(fileContent.columnCount > 1) {
//            fileContent.lines.let { nFileContentRows -> CsvFileContentAdapter(nFileContentRows) }
//        } else {
//            fileContent.lines.let { nFileContentRows -> FileContentAdapter(nFileContentRows) }
//        }
//    }

        return rootView
    }

    private fun itemClicked(entry: Entry, context: Context, rootView: View) {
        Toast.makeText(context, "Total: ${entry.word}", Toast.LENGTH_SHORT).show()
        entryViewModel.updateSelected(entry.id)
        //}


        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view_calcu_list)
        val adapter = CalcuListAdapter(context) { entry: Entry -> itemClicked(entry) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

//        val calculationFragment = CalculationFragment()
//        calculationFragment.clickListener = { selectOperation() }
//        calculationFragment.result =
//            calculations[currentOperation] ?: error("no operation with name $currentOperation")
//        calculationFragment.operation = currentOperation
//        fragManager.beginTransaction().add(R.id.calculation_container, calculationFragment).commit()

        //entryViewModel = ViewModelProviders.of(this).get(CalcuListAllViewModel::class.java)
        entryViewModel = ViewModelProvider(this).get(CalcuListAllViewModel::class.java)
        entryViewModel.allEntries.observe(viewLifecycleOwner, Observer
        { entries ->
            entries?.reversed().let { adapter.setEntries(it as List<Entry>) }
            calculations["Count"] = (adapter.itemCount * 100).toLong()

            if (calculations["Count"] == 0L) {
                calculations["Total"] = 0
                calculations["Min"] = 0
                calculations["Max"] = 0
                calculations["Mean"] = 0
                calculations["Count"] = 0
//                calculationFragment.result =
//                    calculations[currentOperation] ?: error("no operation with name $currentOperation")
//                calculationFragment.operation = currentOperation
//                fragManager.beginTransaction().replace(R.id.calculation_container, calculationFragment).commit()
            }
        })

        editTextValue = rootView.findViewById(R.id.edit_text_value)
        val textViewWord =
            rootView.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view_name)
        /* keyboard input "Enter" */
        editTextValue.setOnEditorActionListener()
        { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage(rootView)
                editTextValue.text.clear()
                textViewWord.text.clear()
                textViewWord.requestFocus()
                handled = true
            }
            handled
        }
        /* all them stats */

        entryViewModel.countSelected.observe(
            viewLifecycleOwner, Observer
            { count ->
                getCountSelected = count
            }
        )

        entryViewModel.total.observe(
            viewLifecycleOwner, Observer
            { total ->
                calculations["Total"] = total
                updateFragment()
            }
        )

        entryViewModel.min.observe(
            viewLifecycleOwner, Observer
            { min ->
                calculations["Min"] = min
                updateFragment()
            }
        )

        entryViewModel.max.observe(
            viewLifecycleOwner, Observer
            { max ->
                calculations["Max"] = max
                updateFragment()
            }
        )

        entryViewModel.mean.observe(
            viewLifecycleOwner, Observer
            { mean ->
                calculations["Mean"] = mean
                updateFragment()
            }
        )
    }

    private fun sendMessage(rootView: View) {
        /* show message if entry empty */
        if (TextUtils.isEmpty(editTextValue.text)) {
            Toast.makeText(this.context, R.string.toast_no_input, Toast.LENGTH_SHORT).show()
        } else {
            /* get entries for word and value */
            val autoCompleteTextViewName =
                rootView.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view_name)
            val entryLeft = autoCompleteTextViewName.text.toString()
            val entryRight = editTextValue.text.toString()
            try {
                /* fill databate */
                val value = encodeDoubleStringAsInt(entryRight, 2)
                // TODO: get name of list
                val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar_calcu_list)
                val autoCompleteTextListName =
                    toolbar.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_calcu_list)
                val listName = autoCompleteTextListName.text.toString()
                entryViewModel.insert(Entry(listName, entryLeft, value))
            } catch (e: NumberFormatException) {
                val msg = "String \"$entryRight\" is not an acceptable number"
                Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
                Log.e("e", msg)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_save -> {
                saveAsCsv()
                return true
            }
            R.id.action_delete_sweep -> {
                clearSelectedWithApproval()
                return true
            }
            R.id.action_select_all -> {
                allEntriesSelected()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun saveAsCsv() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            //TODo
            Log.e("CalcuListActivity", "External Storage writable. - but TODO")
//            val saveFileDialogFragment = SaveFileDialogFragment()
//            saveFileDialogFragment.show(fragManager, "saveFileDialogFragment")
        } else
            Log.e("CalcuListActivity", "External Storage not writable.")
    }


    private fun allEntriesSelected() {
        if (calculations["Count"]?.toInt() == getCountSelected) {
            entryViewModel.updateSelectedAll()
        } else {
            entryViewModel.updateSelectedAllTrue()
        }
    }

    private fun clearSelectedWithApproval() {
        AlertDialog.Builder(this.context)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.dialog_delete_all_selected_items)
            .setMessage(R.string.dialog_explanation)
            .setPositiveButton(
                R.string.dialog_yes
            ) { _, _ ->
                clearSelected()
                Toast.makeText(this.context, getString(R.string.deleted), Toast.LENGTH_LONG)
                    .show()
            }
            .setNegativeButton(
                R.string.dialog_no
            ) { _, _ ->
                Toast.makeText(
                    this.context,
                    getString(R.string.nothing_happend),
                    Toast.LENGTH_LONG
                ).show()
            }
            .show()
    }

    private fun clearSelected() {
        entryViewModel.deleteSelected()
    }


    private fun itemClicked(entry: Entry) {
        Toast.makeText(this.context, "Total: ${calculations["Total"]}", Toast.LENGTH_SHORT).show()

        entryViewModel.updateSelected(entry.id)
    }

    private fun selectOperation() {
//        val chooseCalculationDialogFragment = ChooseCalculationDialogFragment()
//        chooseCalculationDialogFragment.show(fragManager, "chooseCalculationDialogFragment")
    }

    override fun onFinishingListDialog(choice: Int) {
        currentOperation = resources.getStringArray(R.array.operations)[choice]
        if (currentOperation == "Percentage") {
            val choosePercentageDialogFragment = ChoosePercentageDialogFragment(currentPercent)
//            choosePercentageDialogFragment.show(fragManager, "choosePercentageDialogFragment")
        }
        updateFragment()
    }

    private fun updateFragment() {
        val calculationFragment = CalculationFragment()
        calculationFragment.clickListener = { selectOperation() }
        if (currentOperation == "Percentage") {
            val total = calculations["Total"] ?: error("no operation with name $currentOperation")
            calculationFragment.result = (total / 100) * currentPercent
            calculationFragment.operation = "$currentPercent %"
            fragManager.beginTransaction().replace(R.id.frame_layout_calculation_container, calculationFragment)
                .commit()
        } else {
            calculationFragment.result =
                calculations[currentOperation] ?: error("no operation with name $currentOperation")
            calculationFragment.operation = currentOperation
            fragManager.beginTransaction().replace(R.id.frame_layout_calculation_container, calculationFragment)
                .commit()
        }
    }

    override fun onFinishingNumberDialog(tens: Int, ones: Int) {
        currentPercent = tens * 10 + ones
        updateFragment()
    }

    private fun writeFile(fileName: String) {
        try {
            val myFile = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                ), fileName
            )
            myFile.createNewFile()
            if (myFile.exists()) {
                Log.d("d", "\t\tMaking File: ${myFile.path}")
                val out = FileOutputStream(myFile)
                entryViewModel.allEntries.observe(
                    this, Observer { entries ->
                        for (entry in entries) {
                            out.write("$entry\n".toByteArray())
                        }
                    }
                )
                out.flush()
                out.close()
            } else {
                Log.d("d", "\t\t${myFile.name} does not exist")
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }

    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_FILE_ID = "item_id"
    }

    override fun onDialogPositiveClick(dialog: String, context: Context) {
        writeFile(dialog)
    }


}


