package org.cipo.statsit.calcu_list.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry_table")
class Entry(
    @field:ColumnInfo(name = "list")
    var list: String?,
    @field:ColumnInfo(name = "word")
    var word: String?,
    @field:ColumnInfo(name = "value")
    var value: Long?,
    @field:ColumnInfo(name = "selected")
    var selected: Boolean? = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        val decodedValue = value?.let { decodeIntAsString(it) }
        return "$word, $decodedValue"
    }

}