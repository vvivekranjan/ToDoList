package com.example.todolistapp.ui.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataItemDao {
    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun getAllItems(): LiveData<List<DataItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataItem: DataItem)

    @Delete
    suspend fun delete(dataItem: DataItem)

    @Update
    suspend fun update(dataItem: DataItem)
}
