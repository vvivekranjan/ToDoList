package com.example.todolistapp.ui.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecycleDao {
    @Query("""SELECT * FROM recycle_table ORDER BY CASE 
            WHEN priority = 'HIGH' THEN 1
            WHEN priority = 'MEDIUM' THEN 2
            ELSE 3 END""")
    fun getTasksSortedByPriority(): LiveData<List<Recycle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataItem: Recycle)

    @Delete
    suspend fun delete(dataItem: Recycle)

    @Query("UPDATE recycle_table SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateCheckedStatus(id: Int, isChecked: Boolean)

    @Query("DELETE FROM recycle_table")
    suspend fun deleteAll()
}