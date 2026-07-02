package com.astralauncher.app.core.di

import android.content.Context
import androidx.room.Room
import com.astralauncher.app.data.local.AstraDatabase
import com.astralauncher.app.data.local.dao.NoteDao
import com.astralauncher.app.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAstraDatabase(@ApplicationContext context: Context): AstraDatabase {
        return Room.databaseBuilder(
            context,
            AstraDatabase::class.java,
            AstraDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AstraDatabase): TaskDao {
        return database.taskDao
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: AstraDatabase): NoteDao {
        return database.noteDao
    }
}
