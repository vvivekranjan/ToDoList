package com.example.todolistapp.ui.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataItemDao {
    @Query("SELECT * FROM todo_table")
    fun getAllItems(): LiveData<List<DataItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataItem: DataItem)

    @Delete
    suspend fun delete(dataItem: DataItem)

    @Query("UPDATE todo_table SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateCheckedStatus(id: Int, isChecked: Boolean)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}
