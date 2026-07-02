package com.astralauncher.app.data.repository

import com.astralauncher.app.data.local.dao.TaskDao
import com.astralauncher.app.data.local.entity.TaskEntity
import com.astralauncher.app.domain.model.Task
import com.astralauncher.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getPinnedTasks(): Flow<List<Task>> {
        return taskDao.getPinnedTasks().map { entities -> entities.map { it.toDomain() } }
    }

    override fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks(query).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }
}

fun TaskEntity.toDomain() = Task(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isPinned = isPinned,
    priority = priority,
    dueDateMillis = dueDateMillis,
    createdAt = createdAt
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isPinned = isPinned,
    priority = priority,
    dueDateMillis = dueDateMillis,
    createdAt = createdAt
)
