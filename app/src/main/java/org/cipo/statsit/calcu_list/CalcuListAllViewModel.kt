package org.cipo.statsit.calcu_list

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import org.cipo.statsit.calcu_list.db.Entry
import org.cipo.statsit.calcu_list.db.EntryRepository
import org.cipo.statsit.calcu_list.db.EntryRoomDatabase

class CalcuListAllViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EntryRepository
    val allEntries: LiveData<List<Entry>>

        val countSelected: LiveData<Int>
        val total: LiveData<Long>
        val min: LiveData<Long>
        val max: LiveData<Long>
        val mean: LiveData<Long>

    init {
        val entryDao = EntryRoomDatabase.getDatabase(application, viewModelScope).entryDao()
        repository = EntryRepository(entryDao)
        allEntries = repository.allEntries
        countSelected = repository.countSelected
        total = repository.total
        min = repository.min
        max = repository.max
        mean = repository.mean
    }

    fun insert(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    fun delete(entry: Entry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun update(list: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateList(list)
    }

    fun updateSelected(entryId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSelected(entryId)
    }

    fun deleteSelected() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSelected()
    }

    fun updateSelectedAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSelectedAll()
    }

    fun updateSelectedAllTrue() = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSelectedAllTrue()
    }


}