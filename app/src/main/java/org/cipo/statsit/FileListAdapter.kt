package org.cipo.statsit

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.cipo.statsit.calcu_list.CalcuListActivity


class FileListAdapter internal constructor(
    private val parentActivity: MainActivity,
    private val values: List<FileList.FileItem>,
    private val twoPane: Boolean
) : RecyclerView.Adapter<FileListAdapter.FileNameViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(parentActivity)

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as FileList.FileItem
            // TODO
//            if (twoPane) {
//                val fragment = CalcuListFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(CalcuListFragment.ARG_FILE_ID, item.name)
//                    }
//                }
//                parentActivity.supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit()
//            } else {
                val intent = Intent(v.context, CalcuListActivity::class.java).apply {
                    putExtra("item_id", item.name)
//                    putExtra(CalcuListFragment.ARG_FILE_ID, item.name)
                }
                v.context.startActivity(intent)
//            }
        }
    }


    inner class FileNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val textViewCountPart: TextView = itemView.findViewById(R.id.item_detail)
        private val textViewCountPart: TextView = itemView.findViewById(R.id.text_view_file_list_line_count)
        private val textViewFilePart: TextView = itemView.findViewById(R.id.text_view_file_list_file_name)

        fun bind(item: FileList.FileItem) = with(itemView) {

            textViewCountPart.text = item.lineCount.toString()
            textViewFilePart.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileNameViewHolder {
        val itemView = inflater.inflate(R.layout.file_list_item, parent, false)
        return FileNameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileNameViewHolder, position: Int) {
        holder.bind(values[position])

        with(holder.itemView) {
            tag = values[position]
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

}