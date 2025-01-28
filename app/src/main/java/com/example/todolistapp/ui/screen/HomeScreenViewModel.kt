package com.example.todolistapp.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.ui.data.DataItem
import com.example.todolistapp.ui.data.DataItemRepository
import com.example.todolistapp.ui.data.RecycleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RecycleBin : Screen("recycle_bin")
//    object AddTaskList : Screen("work_list/{workListId}") {
//        fun createRoute(workListId: Int) = "work_list/$workListId"
//    }
}

class HomeScreenViewModel(
    private val repository: DataItemRepository,
    private val binRepository: RecycleRepository
) : ViewModel() {
    val workList: LiveData<List<DataItem>> = repository.allItems

    fun add(text: String, priority: String, deadLine: String) {
        viewModelScope.launch {
            val newItem = DataItem(
                message = text,
                priority = priority,
                deadLine = deadLine
            )
            repository.insert(newItem)
        }
    }

//    fun insert(dataItem: WorkList) {
//        viewModelScope.launch {
//            workListDao.insert(dataItem)
//        }
//    }

    // Toggle the checked status
    fun toggleChecked(dataItem: DataItem) {
        viewModelScope.launch {
            repository.updateCheckedStatus(dataItem.id, !dataItem.isChecked)
        }
    }

    fun delete(dataItem: DataItem) {
        viewModelScope.launch {
            delay(300)
            RecycleBinViewModel(binRepository).insert(dataItem)
            repository.delete(dataItem)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            delay(300)
            workList.value?.let { RecycleBinViewModel(binRepository).insert(it) }
            repository.deleteAll()
        }
    }
}

