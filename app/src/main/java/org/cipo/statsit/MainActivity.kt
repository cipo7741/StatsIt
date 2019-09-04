package org.cipo.statsit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.cipo.statsit.calcu_list.CalcuListActivity

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [CalcuListActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MainActivity : AppCompatActivity() {


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_calcu_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val newCalcuListButton = findViewById<Button>(R.id.button_file_list)
        newCalcuListButton.setOnClickListener { view ->
//            val item = view.tag as FileList.FileItem
//            Toast.makeText(view.context, item.name, Toast.LENGTH_LONG).show()
//            if (twoPane) {
            // TODO
//            } else {
            val intent = Intent(view.context, CalcuListActivity::class.java).apply {
                putExtra("item_id", "")
//                    putExtra(CalcuListFragment.ARG_FILE_ID, item.name)
            }
            view.context.startActivity(intent)
        }

        val itemDetailContainer = findViewById<FrameLayout>(R.id.item_detail_container)
        if (itemDetailContainer != null) {
            twoPane = true
        }
        val recyclerViewItemList = findViewById<RecyclerView>(R.id.recycler_view_main)
        val internalFilesDirectory = recyclerViewItemList.context.filesDir
        Log.d("MainActivity", "\t\t${internalFilesDirectory.absolutePath}")
        recyclerViewItemList.adapter =
            FileListAdapter(this, FileList(internalFilesDirectory).files, twoPane)
    }

//    override fun onDialogPositiveClick(dialog: String) {
//        val newCalcuListButton = findViewById<Button>(R.id.button_file_list)
//        val intent = Intent(newCalcuListButton.context, CalcuListActivity::class.java).apply {
//            putExtra("item_id", dialog)
////            putExtra(CalcuListFragment.ARG_FILE_ID, dialog)
//        }
//        newCalcuListButton.context.startActivity(intent)
//    }
}
