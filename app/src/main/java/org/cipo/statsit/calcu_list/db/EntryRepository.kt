package org.cipo.statsit.calcu_list.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class EntryRepository(private val wordDao: EntryDao, private val currentList: String = "") {

    val allEntries: LiveData<List<Entry>> = if (currentList.isEmpty()) wordDao.getAllEntries() else wordDao.getAllEntries(currentList)
    val countSelected: LiveData<Int> = if (currentList.isEmpty()) wordDao.getCountSelected() else wordDao.getCountSelected(currentList)
    val total: LiveData<Long> = if (currentList.isEmpty()) wordDao.getSumOfValues() else wordDao.getSumOfValues(currentList)
    val min: LiveData<Long> = if (currentList.isEmpty()) wordDao.getMinOfValues() else wordDao.getMinOfValues(currentList)
    val max: LiveData<Long> = if (currentList.isEmpty()) wordDao.getMaxOfValues() else wordDao.getMaxOfValues(currentList)
    val mean: LiveData<Long> = if (currentList.isEmpty()) wordDao.getMeanOfValues() else wordDao.getMeanOfValues(currentList)

    @WorkerThread
    suspend fun insert(entry: Entry) {
        wordDao.insert(entry)
    }

    @WorkerThread
    fun delete(entry: Entry) {
        wordDao.delete(entry.id)
    }

    @WorkerThread
    fun deleteAll() {
        wordDao.deleteAll(currentList)
    }

    @WorkerThread
    fun updateSelected(entryId: Int) {
        wordDao.updateSelected(entryId)
    }

    @WorkerThread
    fun deleteSelected() {
        wordDao.deleteSelected(currentList)
    }

    @WorkerThread
    fun updateSelectedAll() {
        wordDao.updateSelectedAll(currentList)
    }

    @WorkerThread
    fun updateSelectedAllTrue() {
        wordDao.updateSelectedAllTrue(currentList)
    }

}