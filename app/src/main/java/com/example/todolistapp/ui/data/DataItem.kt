package com.example.todolistapp.ui.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.Date

data class DataItem(
    val id: Int,
    val message: String,
    val timeStamp: Date,
    var isChecked: MutableState<Boolean> = mutableStateOf(false)
)