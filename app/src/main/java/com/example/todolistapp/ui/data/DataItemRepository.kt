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

    suspend fun update(dataItem: DataItem) {
        dataItemDao.update(dataItem)
    }
}
