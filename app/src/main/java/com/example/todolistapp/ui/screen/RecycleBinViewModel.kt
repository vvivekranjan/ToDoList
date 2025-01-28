package com.example.todolistapp.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.ui.data.DataItem
import com.example.todolistapp.ui.data.Recycle
import com.example.todolistapp.ui.data.RecycleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecycleBinViewModel(private val repository: RecycleRepository) : ViewModel() {
    val binList: LiveData<List<Recycle>> = repository.allItems

    fun insert(workData: DataItem) {
        viewModelScope.launch {
            val newItem = Recycle(
                message = workData.message,
                timeStamp = workData.timeStamp,
                isChecked = false,
                priority = workData.priority,
                deadLine = workData.deadLine
            )
            repository.insert(newItem)
        }
    }

    fun insert(workData: List<DataItem>) {
        viewModelScope.launch {
            for (data in workData) {
                val newItem = Recycle(
                    message = data.message,
                    timeStamp = data.timeStamp,
                    isChecked = false,
                    priority = data.priority,
                    deadLine = data.deadLine
                )
                repository.insert(newItem)
            }
        }
    }

    // Toggle the checked status
    fun toggleChecked(dataItem: Recycle) {
        viewModelScope.launch {
            repository.updateCheckedStatus(dataItem.id, !dataItem.isChecked)
        }
    }

    fun delete(dataItem: Recycle) {
        viewModelScope.launch {
            delay(300)
            repository.delete(dataItem)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            delay(300)
            repository.deleteAll()
        }
    }
}