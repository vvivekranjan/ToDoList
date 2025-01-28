package com.example.todolistapp

import android.content.Context
import androidx.compose.runtime.Composable
import com.example.todolistapp.ui.data.AppDatabase
import com.example.todolistapp.ui.data.DataItemRepository
import com.example.todolistapp.ui.data.RecycleDatabase
import com.example.todolistapp.ui.data.RecycleRepository
import com.example.todolistapp.ui.screen.HomeScreenViewModel
import com.example.todolistapp.ui.screen.Navigation
import com.example.todolistapp.ui.screen.RecycleBinViewModel

@Composable
fun ToDoListApp(context: Context) {
    val db = AppDatabase.getDatabase(context)
    val bin = RecycleDatabase.getDatabase(context)
    val repository = DataItemRepository(db.DataItemDao())
    val binRepository = RecycleRepository(bin.RecycleDao())
    val viewModel = HomeScreenViewModel(repository, binRepository)
    val binViewModel = RecycleBinViewModel(binRepository)

    Navigation(viewModel, binViewModel)
}
