package com.example.todolistapp.ui.data

import androidx.lifecycle.LiveData

class DataItemRepository(private val dataItemDao: DataItemDao) {

    val allItems: LiveData<List<DataItem>> = dataItemDao.getAllItems()

    suspend fun insert(dataItem: DataItem) {
        dataItemDao.insert(dataItem)
    }

    suspend fun delete(dataItem: DataItem) {
        dataItemDao.delete(dataItem)
    }

    // New method to update the checkbox state
    suspend fun updateCheckedStatus(id: Int, isChecked: Boolean) {
        dataItemDao.updateCheckedStatus(id, isChecked)
    }

    suspend fun deleteAll() {
        dataItemDao.deleteAll()
    }
}
