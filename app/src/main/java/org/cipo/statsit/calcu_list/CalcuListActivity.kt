package org.cipo.statsit.calcu_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.cipo.statsit.MainActivity

import org.cipo.statsit.calcu_list.fragments.SaveFileDialogFragment
import org.cipo.statsit.R
import org.cipo.statsit.calcu_list.db.Entry
import org.cipo.statsit.calcu_list.db.encodeDoubleStringAsInt
import org.cipo.statsit.calcu_list.fragments.CalculationFragment
import org.cipo.statsit.calcu_list.fragments.ChooseCalculationDialogFragment
import org.cipo.statsit.calcu_list.fragments.ChoosePercentageDialogFragment

import java.io.File
import java.io.FileOutputStream

class CalcuListActivity : AppCompatActivity(),
    ChooseCalculationDialogFragment.ChoosenOperationListener,
    ChoosePercentageDialogFragment.ChoosenPercentageListener {

    private val fragManager = supportFragmentManager

    private lateinit var entryViewModel: CalcuListByListNameViewModel

    private var currentListName = "default"

    private var currentOperationName = "Total"

    private var calculations: MutableMap<String, Long> = mutableMapOf(
        "Total" to 0L,
        "Min" to 0L,
        "Max" to 0L,
        "Mean" to 0L,
        "Count" to 0L
    )

    private var currentPercent = 0

    private var countSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcu_list)
        /* toolbar */
        currentListName = intent.getStringExtra("item_id")
        val toolbar = findViewById<Toolbar>(R.id.toolbar_calcu_list)
        toolbar.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_calcu_list).setText(currentListName)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /* keyboard visible */
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        /*list adapter*/
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_calcu_list)
        val adapter = CalcuListAdapter(this) { entry: Entry -> itemClicked(entry) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        /* calculation on values with operation name and result */
        val calculationFragment = CalculationFragment()
        calculationFragment.clickListener = { selectOperation() }
        calculationFragment.result =
            calculations[currentOperationName] ?: error("no operation with name $currentOperationName")
        calculationFragment.operation = currentOperationName
        fragManager.beginTransaction().add(R.id.frame_layout_calculation_container, calculationFragment).commit()

//        entryViewModel = ViewModelProviders.of(this, CalcuListViewModelFactory(CalcuListAllViewModel::class.java,currentListName).get(CalcuListAllViewModel::class.java)


        entryViewModel =  ViewModelProviders.of(this, CalcuListViewModelFactory(application, currentListName)).get(CalcuListByListNameViewModel::class.java)

        entryViewModel.allEntries.observe(this, Observer { entries ->
            entries?.reversed().let { adapter.setEntries(it as List<Entry>) }
            calculations["Count"] = (adapter.itemCount * 100).toLong()

            if (calculations["Count"] == 0L) {
                calculations["Total"] = 0
                calculations["Min"] = 0
                calculations["Max"] = 0
                calculations["Mean"] = 0
                calculations["Count"] = 0
                calculationFragment.result =
                    calculations[currentOperationName] ?: error("no operation with name $currentOperationName")
                calculationFragment.operation = currentOperationName
                fragManager.beginTransaction().replace(R.id.frame_layout_calculation_container, calculationFragment).commit()
            }
        })



        val editTextValue = findViewById<EditText>(R.id.edit_text_value)
        val textViewWord = findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view_name)

        if(currentListName.isNotBlank()){
            Toast.makeText(this, currentListName, Toast.LENGTH_LONG).show()
            textViewWord.requestFocus()
        }

        /* keyboard input "Enter" */
        editTextValue.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage(textViewWord.text.toString(), editTextValue.text.toString())
                editTextValue.text.clear()
                textViewWord.text.clear()
                textViewWord.requestFocus()
                handled = true
            }
            handled
        }

        /* all them stats */
        entryViewModel.countSelected.observe(
            this, Observer { selected ->
                countSelected = selected
            }
        )

        entryViewModel.total.observe(
            this, Observer { total ->
                calculations["Total"] = total
                updateFragment()
            }
        )

        entryViewModel.min.observe(
            this, Observer { min ->
                calculations["Min"] = min
                updateFragment()
            }
        )

        entryViewModel.max.observe(
            this, Observer { max ->
                calculations["Max"] = max
                updateFragment()
            }
        )

        entryViewModel.mean.observe(
            this, Observer { mean ->
                calculations["Mean"] = mean
                updateFragment()
            }
        )
    }

    private fun sendMessage(wordEntryLeft: String, valueEntryRight: String) {
        /* show message if entry empty */
        if (TextUtils.isEmpty(valueEntryRight)) {
            Toast.makeText(applicationContext, R.string.toast_no_input, Toast.LENGTH_SHORT).show()
        } else {
            /* get entries for word and value */
            try {
                /* fill database */
                val value = encodeDoubleStringAsInt(valueEntryRight, 2)
                entryViewModel.insert(Entry(currentListName, wordEntryLeft, value))
            } catch (e: NumberFormatException) {
                val msg = "String \"$valueEntryRight\" is not an acceptable number"
                Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
                Log.e("e", msg)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, MainActivity::class.java))
                writeFile(currentListName, this)
                true
            }
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
        val saveFileDialogFragment = SaveFileDialogFragment()
        saveFileDialogFragment.show(fragManager, "saveFileDialogFragment")
    }


    private fun allEntriesSelected() {
        if (calculations["Count"]?.toInt() == countSelected) {
            entryViewModel.updateSelectedAll()
        } else {
            entryViewModel.updateSelectedAllTrue()
        }
    }

    private fun clearSelectedWithApproval() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.dialog_delete_all_selected_items)
            .setMessage(R.string.dialog_explanation)
            .setPositiveButton(
                R.string.dialog_yes
            ) { _, _ ->
                clearSelected()
                Toast.makeText(applicationContext, getString(R.string.deleted), Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(
                R.string.dialog_no
            ) { _, _ ->
                Toast.makeText(applicationContext, getString(R.string.nothing_happend), Toast.LENGTH_LONG).show()
            }
            .show()
    }

    private fun clearSelected() {
        entryViewModel.deleteSelected()
    }


    private fun itemClicked(entry: Entry) {
        Toast.makeText(this, "Total: ${calculations["Total"]}", Toast.LENGTH_SHORT).show()
        entryViewModel.updateSelected(entry.id)
    }

    private fun selectOperation() {
        val chooseCalculationDialogFragment = ChooseCalculationDialogFragment()
        chooseCalculationDialogFragment.show(fragManager, "chooseCalculationDialogFragment")
    }

    override fun onFinishingListDialog(choice: Int) {
        currentOperationName = resources.getStringArray(R.array.operations)[choice]
        if (currentOperationName == "Percentage") {
            val choosePercentageDialogFragment = ChoosePercentageDialogFragment(currentPercent)
            choosePercentageDialogFragment.show(fragManager, "choosePercentageDialogFragment")
        }
        updateFragment()
    }

    private fun updateFragment() {
        val calculationFragment = CalculationFragment()
        calculationFragment.clickListener = { selectOperation() }
        if (currentOperationName == "Percentage") {
            val total = calculations["Total"] ?: error("no operation with name $currentOperationName")
            calculationFragment.result = (total / 100) * currentPercent
            calculationFragment.operation = "$currentPercent %"
            fragManager.beginTransaction().replace(R.id.frame_layout_calculation_container, calculationFragment).commit()
        } else {
            calculationFragment.result =
                calculations[currentOperationName] ?: error("no operation with name $currentOperationName")
            calculationFragment.operation = currentOperationName
            fragManager.beginTransaction().replace(R.id.frame_layout_calculation_container, calculationFragment).commit()
        }
    }

    override fun onFinishingNumberDialog(tens: Int, ones: Int) {
        currentPercent = tens * 10 + ones
        updateFragment()
    }

    private fun writeFile(fileName: String, context: Context) {
        // TODO: check if something changed
        val internalFilesDirectory = context.filesDir
        try {
            val myFile = File(internalFilesDirectory, fileName)
            if (!myFile.exists()){
                myFile.createNewFile()
            }
            Log.d("d", "\t\tCreated file: ${myFile.path}")
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
        } catch (e: Exception) {
            Log.d("d", "Something went wrong: ${e.message}!")
            e.printStackTrace()

        }
    }


}


