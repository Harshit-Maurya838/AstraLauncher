package com.astralauncher.app.core.di

import com.astralauncher.app.data.repository.AppRepositoryImpl
import com.astralauncher.app.data.repository.SettingsRepositoryImpl
import com.astralauncher.app.domain.repository.AppRepository
import com.astralauncher.app.domain.repository.SettingsRepository
import com.astralauncher.app.data.repository.TaskRepositoryImpl
import com.astralauncher.app.data.repository.NoteRepositoryImpl
import com.astralauncher.app.data.repository.CalendarRepositoryImpl
import com.astralauncher.app.domain.repository.TaskRepository
import com.astralauncher.app.domain.repository.NoteRepository
import com.astralauncher.app.domain.repository.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppRepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl
    ): CalendarRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
