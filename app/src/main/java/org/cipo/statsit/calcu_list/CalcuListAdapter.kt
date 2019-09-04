package org.cipo.statsit.calcu_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import org.cipo.statsit.R
import org.cipo.statsit.calcu_list.db.Entry
import org.cipo.statsit.calcu_list.db.decodeIntAsString

class CalcuListAdapter internal constructor(
    context: Context,
    private val clickListener: (Entry) -> Unit
) : RecyclerView.Adapter<CalcuListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var entries = emptyList<Entry>() // Cached copy of entries

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemViewLeft: TextView = itemView.findViewById(R.id.textView_recyclerview_item_left)

        private val itemViewRight: TextView = itemView.findViewById(R.id.textView_recyclerview_item_right)

        fun bind(item: Entry, listener: (Entry) -> Unit) = with(itemView) {
            itemViewLeft.text = item.word
            itemViewRight.text = decodeIntAsString(item.value!!, 2)
            setOnClickListener { listener(item) }
            if(item.selected!!){
                itemViewLeft.background = ContextCompat.getDrawable(context, R.color.colorPrimaryTranspi)
                itemViewRight.background = ContextCompat.getDrawable(context, R.color.colorPrimaryTranspi)
            } else {
                itemViewLeft.background = ContextCompat.getDrawable(context, android.R.color.transparent)
                itemViewRight.background = ContextCompat.getDrawable(context, android.R.color.transparent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.calcu_list_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        // Populate ViewHolder with data that corresponds to the position in the list
        // which we are told to load
        holder.bind(entries[position], clickListener)
//        (holder as WordViewHolder).bind(entries[position], clickListener, buttonListener)
    }

    internal fun setEntries(entries: List<Entry>) {
        this.entries = entries
        notifyDataSetChanged()
    }

    override fun getItemCount() = entries.size


}