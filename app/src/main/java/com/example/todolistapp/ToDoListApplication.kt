package com.example.todolistapp

import android.content.Context
import androidx.compose.runtime.Composable
import com.example.todolistapp.ui.data.AppDatabase
import com.example.todolistapp.ui.data.DataItemRepository
import com.example.todolistapp.ui.screen.HomeScreen
import com.example.todolistapp.ui.screen.HomeScreenViewModel

@Composable
fun ToDoListApp(context: Context) {
    val db = AppDatabase.getDatabase(context)
    val repository = DataItemRepository(db.DataItemDao())
    val viewModel = HomeScreenViewModel(repository)

    HomeScreen(viewModel)
}
