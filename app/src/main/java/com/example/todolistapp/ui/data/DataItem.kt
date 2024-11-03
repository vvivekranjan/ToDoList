package com.example.todolistapp.ui.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Entity(tableName = "todo_table")
data class DataItem(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    @NotNull
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "timeStamp")
    val timeStamp: String = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault()).format(Date()),
    @ColumnInfo(name = "isChecked")
    var isChecked: Boolean = false
)