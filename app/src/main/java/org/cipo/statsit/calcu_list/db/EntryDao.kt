package org.cipo.statsit.calcu_list.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EntryDao {

    @Query("SELECT * from entry_table")
    fun getAllEntries(): LiveData<List<Entry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Entry)

    @Query("DELETE FROM entry_table where id =:entryID")
    fun delete(entryID: Int)

    @Query("DELETE FROM entry_table where selected = 1")
    fun deleteSelected()

    @Query("DELETE FROM entry_table")
    fun deleteAll()

    @Query("UPDATE entry_table SET selected = NOT selected where id =:entryId")
    fun updateSelected(entryId: Int)

    @Query("UPDATE entry_table SET selected = 1 where selected != 1")
    fun updateSelectedAllTrue()

    @Query("UPDATE entry_table SET selected = NOT selected")
    fun updateSelectedAll()

    @Query("SELECT COUNT(selected) FROM entry_table where selected = 1")
    fun getCountSelected() : LiveData<Int>

    @Query("SELECT COALESCE(SUM(value),0) FROM entry_table")
    fun getSumOfValues(): LiveData<Long>

    @Query("SELECT COALESCE(MIN(value),0) FROM entry_table")
    fun getMinOfValues(): LiveData<Long>

    @Query("SELECT COALESCE(MAX(value),0) FROM entry_table")
    fun getMaxOfValues(): LiveData<Long>

    @Query("SELECT COALESCE(AVG(value),0) FROM entry_table")
    fun getMeanOfValues(): LiveData<Long>

// ####################################################################3

    @Query("SELECT * from entry_table where list = :list")
    fun getAllEntries(list: String): LiveData<List<Entry>>

    @Query("DELETE FROM entry_table where id =:entryID and list = :list")
    fun delete(entryID: Int, list: String)

    @Query("UPDATE entry_table SET list = :newList where list = :oldList")
    fun update(oldList: String, newList: String)

    @Query("DELETE FROM entry_table where selected = 1 and list = :list")
    fun deleteSelected(list: String)

    @Query("DELETE FROM entry_table where list = :list")
    fun deleteAll(list: String)

    @Query("UPDATE entry_table SET selected = NOT selected where id =:entryId and list = :list")
    fun updateSelected(entryId: Int, list: String)

    @Query("UPDATE entry_table SET selected = 1 where selected != 1 and list = :list")
    fun updateSelectedAllTrue(list: String)

    @Query("UPDATE entry_table SET selected = NOT selected where list = :list")
    fun updateSelectedAll(list: String)

    @Query("SELECT COUNT(selected) FROM entry_table where selected = 1 and list = :list")
    fun getCountSelected(list: String) : LiveData<Int>

    @Query("SELECT COALESCE(SUM(value),0) FROM entry_table where list = :list")
    fun getSumOfValues(list: String): LiveData<Long>

    @Query("SELECT COALESCE(MIN(value),0) FROM entry_table where list = :list")
    fun getMinOfValues(list: String): LiveData<Long>

    @Query("SELECT COALESCE(MAX(value),0) FROM entry_table where list = :list")
    fun getMaxOfValues(list: String): LiveData<Long>

    @Query("SELECT COALESCE(AVG(value),0) FROM entry_table where list = :list")
    fun getMeanOfValues(list: String): LiveData<Long>


}