package com.example.todolistapp.ui.data

import java.time.Instant
import java.util.Date

object DataManager {

    private val toDoList = mutableListOf<DataItem>()

    fun getAllItems(): List<DataItem> {
        return toDoList
    }

    fun addItems(message: String) {
        toDoList.add(DataItem(System.currentTimeMillis().toInt(), message, Date.from(Instant.now())))
    }

    fun deleteItems(id: Int) {
        toDoList.removeIf{
            it.id == id
        }
    }

}