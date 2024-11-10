package com.example.todolistapp.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.ui.data.DataItem
import com.example.todolistapp.ui.data.DataItemRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: DataItemRepository) : ViewModel() {
    val todoList: LiveData<List<DataItem>> = repository.allItems

    fun add(text: String) {
        viewModelScope.launch {
            val newItem = DataItem(message = text)
            repository.insert(newItem)
        }
    }

    // Toggle the checked status
    fun toggleChecked(dataItem: DataItem) {
        viewModelScope.launch {
            repository.updateCheckedStatus(dataItem.id, !dataItem.isChecked)
        }
    }

    fun delete(dataItem: DataItem) {
        viewModelScope.launch {
            repository.delete(dataItem)
        }
    }
}
