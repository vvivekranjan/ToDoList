package com.example.todolistapp.ui.data

import androidx.lifecycle.LiveData

class RecycleRepository(private val dataItemDao: RecycleDao) {

    val allItems: LiveData<List<Recycle>> = dataItemDao.getTasksSortedByPriority()

    suspend fun insert(dataItem: Recycle) {
        dataItemDao.insert(dataItem)
    }

    suspend fun delete(dataItem: Recycle) {
        dataItemDao.delete(dataItem)
    }

    suspend fun updateCheckedStatus(id: Int, isChecked: Boolean) {
        dataItemDao.updateCheckedStatus(id, isChecked)
    }

    suspend fun deleteAll() {
        dataItemDao.deleteAll()
    }
}