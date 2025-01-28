package com.example.todolistapp.ui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recycle::class], version = 1, exportSchema = true)
abstract class RecycleDatabase : RoomDatabase() {
    abstract fun RecycleDao(): RecycleDao

    companion object {
        @Volatile
        private var INSTANCE: RecycleDatabase? = null

        fun getDatabase(context: Context): RecycleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecycleDatabase::class.java,
                    "recycle_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}