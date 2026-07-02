package com.astralauncher.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astralauncher.app.data.local.dao.NoteDao
import com.astralauncher.app.data.local.dao.TaskDao
import com.astralauncher.app.data.local.entity.NoteEntity
import com.astralauncher.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AstraDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val noteDao: NoteDao
    
    companion object {
        const val DATABASE_NAME = "astra_db"
    }
}
