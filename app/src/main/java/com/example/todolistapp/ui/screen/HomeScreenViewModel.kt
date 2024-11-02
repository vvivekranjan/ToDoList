package com.example.todolistapp.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolistapp.ui.data.DataItem
import com.example.todolistapp.ui.data.DataManager

class HomeScreenViewModel: ViewModel() {

    private var _uiState = MutableLiveData<List<DataItem>>()
    val toDoList: LiveData<List<DataItem>> = _uiState

    fun getAllItems() {
        _uiState.value = DataManager.getAllItems().reversed()
    }

    fun addItems(message: String) {
        DataManager.addItems(message)
        getAllItems()
    }

    fun deleteItems(id: Int) {
        DataManager.deleteItems(id)
        getAllItems()
    }

}