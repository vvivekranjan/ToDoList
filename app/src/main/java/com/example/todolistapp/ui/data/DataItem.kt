package com.example.todolistapp.ui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "todo_table")
data class DataItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "timeStamp")
    val timeStamp: String = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault()).format(Date()),
    @ColumnInfo(name = "isChecked")
    var isChecked: Boolean = false,
    @ColumnInfo(name = "priority", defaultValue = "LOW")
    val priority: String = "LOW",
//    @ColumnInfo(name = "deadLine")
//    var deadLine: String? = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault()).format(Date())
)

enum class Priority() {
    HIGH,
    MEDIUM,
    LOW
}