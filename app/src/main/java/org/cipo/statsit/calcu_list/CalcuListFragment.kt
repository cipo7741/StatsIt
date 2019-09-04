package org.cipo.statsit.calcu_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.cipo.statsit.FileList
import org.cipo.statsit.R
import org.cipo.statsit.calcu_list.db.Entry

class CalcuListFragment : Fragment()
//    ,
//    ChooseCalculationDialogFragment.ChoosenOperationListener,
//    ChoosePercentageDialogFragment.ChoosenPercentageListener,
//    SaveFileDialogFragment.NoticeDialogListener
{

//    private lateinit var entryViewModel: CalcuListAllViewModel
//
//    private lateinit var editTextValue: EditText
//
//    private var currentOperation = "Total"
//
//    private var currentPercent = 0
//
//    private var calculations: MutableMap<String, Long> = mutableMapOf(
//        "Total" to 0L,
//        "Min" to 0L,
//        "Max" to 0L,
//        "Mean" to 0L,
//        "Count" to 0L
//    )
//
//    private var getCountSelected: Int = 0
//
////    private val fragManager = supportFragmentManager
//
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
                item = FileList(context!!.filesDir).files_map[it.getString(ARG_FILE_ID)!!]
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

    val adapter = container?.context?.let { CalcuListAdapter(it) { entry: Entry -> itemClicked(entry, recyclerView.context) } }
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

    private fun itemClicked(entry: Entry, context: Context) {
        Toast.makeText(context, "Total: ${entry.word}", Toast.LENGTH_SHORT).show()
//        entryViewModel.updateSelected(entry.id)
    }

//
//        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recyclerView)
//        val adapter = CalcuListAdapter(this) { entry: Entry -> itemClicked(entry) }
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
////        val calculationFragment = CalculationFragment()
////        calculationFragment.clickListener = { selectOperation() }
////        calculationFragment.result =
////            calculations[currentOperation] ?: error("no operation with name $currentOperation")
////        calculationFragment.operation = currentOperation
////        fragManager.beginTransaction().add(R.id.calculation_container, calculationFragment).commit()
//
//        entryViewModel = ViewModelProviders.of(this).get(CalcuListAllViewModel::class.java)
//        entryViewModel.allEntries.observe(this, Observer { entries ->
//            entries?.reversed().let { adapter.setEntries(it as List<Entry>) }
//            calculations["Count"] = (adapter.itemCount * 100).toLong()
//
//            if (calculations["Count"] == 0L) {
//                calculations["Total"] = 0
//                calculations["Min"] = 0
//                calculations["Max"] = 0
//                calculations["Mean"] = 0
//                calculations["Count"] = 0
////                calculationFragment.result =
////                    calculations[currentOperation] ?: error("no operation with name $currentOperation")
////                calculationFragment.operation = currentOperation
////                fragManager.beginTransaction().replace(R.id.calculation_container, calculationFragment).commit()
//            }
//        })
//
//        editTextValue = rootView.findViewById(R.id.editTextValue)
//        val textViewWord = rootView.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewName)
//        /* keyboard input "Enter" */
//        editTextValue.setOnEditorActionListener { _, actionId, _ ->
//            var handled = false
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                sendMessage()
//                editTextValue.text.clear()
//                textViewWord.text.clear()
//                textViewWord.requestFocus()
//                handled = true
//            }
//            handled
//        }
//        /* all them stats */
//        entryViewModel.getCountSelected.observe(
//            this, Observer { count ->
//                getCountSelected = count
//            }
//        )
//
//        entryViewModel.total.observe(
//            this, Observer { total ->
//                calculations["Total"] = total
//                updateFragment()
//            }
//        )
//
//        entryViewModel.min.observe(
//            this, Observer { min ->
//                calculations["Min"] = min
//                updateFragment()
//            }
//        )
//
//        entryViewModel.max.observe(
//            this, Observer { max ->
//                calculations["Max"] = max
//                updateFragment()
//            }
//        )
//
//        entryViewModel.mean.observe(
//            this, Observer { mean ->
//                calculations["Mean"] = mean
//                updateFragment()
//            }
//        )
//    }
//
//    private fun sendMessage() {
//        /* show message if entry empty */
//        if (TextUtils.isEmpty(editTextValue.text)) {
//            Toast.makeText(applicationContext, R.string.toast_no_input, Toast.LENGTH_SHORT).show()
//        } else {
//            /* get entries for word and value */
//            val autoCompleteTextViewName = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewName)
//            val entryLeft = autoCompleteTextViewName.text.toString()
//            val entryRight = editTextValue.text.toString()
//            try {
//                /* fill databate */
//                val value = encodeDoubleStringAsInt(entryRight, 2)
//                entryViewModel.insert(Entry(entryLeft, value))
//            } catch (e: NumberFormatException) {
//                val msg = "String \"$entryRight\" is not an acceptable number"
//                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
//                Log.e("e", msg)
//            }
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            R.id.action_save -> {
//                saveAsCsv()
//                return true
//            }
//            R.id.action_delete_sweep -> {
//                clearSelectedWithApproval()
//                return true
//            }
//            R.id.action_select_all -> {
//                allEntriesSelected()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//
//    private fun saveAsCsv() {
//        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//            val saveFileDialogFragment = SaveFileDialogFragment()
//            saveFileDialogFragment.show(fragManager, "saveFileDialogFragment")
//        } else
//            Log.e("CalcuListActivity", "External Storage not writable.")
//    }
//
//
//
//    private fun allEntriesSelected() {
//        if (calculations["Count"]?.toInt() == getCountSelected) {
//            entryViewModel.updateSelectedAll()
//        } else {
//            entryViewModel.updateSelectedAllTrue()
//        }
//    }
//
//    private fun clearSelectedWithApproval() {
//        AlertDialog.Builder(this)
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .setTitle(R.string.dialog_delete_all_selected_items)
//            .setMessage(R.string.dialog_explanation)
//            .setPositiveButton(
//                R.string.dialog_yes
//            ) { _, _ ->
//                clearSelected()
//                Toast.makeText(applicationContext, getString(R.string.deleted), Toast.LENGTH_LONG).show()
//            }
//            .setNegativeButton(
//                R.string.dialog_no
//            ) { _, _ ->
//                Toast.makeText(applicationContext, getString(R.string.nothing_happend), Toast.LENGTH_LONG).show()
//            }
//            .show()
//    }
//
//    private fun clearSelected() {
//        entryViewModel.deleteSelected()
//    }
//
//
//    private fun itemClicked(entry: Entry) {
//        Toast.makeText(this, "Total: ${calculations["Total"]}", Toast.LENGTH_SHORT).show()
//        entryViewModel.updateSelected(entry.id)
//    }
//
//    private fun selectOperation() {
//        val chooseCalculationDialogFragment = ChooseCalculationDialogFragment()
//        chooseCalculationDialogFragment.show(fragManager, "chooseCalculationDialogFragment")
//    }
//
//    override fun onFinishingListDialog(choice: Int) {
//        currentOperation = resources.getStringArray(R.array.operations)[choice]
//        if (currentOperation == "Percentage") {
//            val choosePercentageDialogFragment = ChoosePercentageDialogFragment(currentPercent)
//            choosePercentageDialogFragment.show(fragManager, "choosePercentageDialogFragment")
//        }
//        updateFragment()
//    }
//
//    private fun updateFragment() {
//        val calculationFragment = CalculationFragment()
//        calculationFragment.clickListener = { selectOperation() }
//        if (currentOperation == "Percentage") {
//            val total = calculations["Total"] ?: error("no operation with name $currentOperation")
//            calculationFragment.result = (total / 100) * currentPercent
//            calculationFragment.operation = "$currentPercent %"
//            fragManager.beginTransaction().replace(R.id.calculation_container, calculationFragment).commit()
//        } else {
//            calculationFragment.result =
//                calculations[currentOperation] ?: error("no operation with name $currentOperation")
//            calculationFragment.operation = currentOperation
//            fragManager.beginTransaction().replace(R.id.calculation_container, calculationFragment).commit()
//        }
//    }
//
//    override fun onFinishingNumberDialog(tens: Int, ones: Int) {
//        currentPercent = tens * 10 + ones
//        updateFragment()
//    }
//
//    override fun onDialogPositiveClick(dialog: String) {
//        writeFile(dialog)
//    }
//
//    private fun writeFile(fileName: String) {
//        try {
//            val myFile = File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS
//            ), fileName)
//            myFile.createNewFile()
//            if (myFile.exists()) {
//                Log.d("d", "\t\tMaking File: ${myFile.path}")
//                val out = FileOutputStream(myFile)
//                entryViewModel.allEntries.observe(
//                    this, Observer { entries ->
//                        for (entry in entries){
//                            out.write("$entry\n".toByteArray())
//                        }
//                    }
//                )
//                out.flush()
//                out.close()
//            } else {
//                Log.d("d", "\t\t${myFile.name} does not exist")
//            }
//        } catch (e: Exception) {
//            println(e.message)
//            e.printStackTrace()
//        }
//
//    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_FILE_ID = "item_id"
    }


}


