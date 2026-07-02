package com.astralauncher.app.data.repository

import com.astralauncher.app.data.local.dao.NoteDao
import com.astralauncher.app.data.local.entity.NoteEntity
import com.astralauncher.app.domain.model.Note
import com.astralauncher.app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getPinnedNotes(): Flow<List<Note>> {
        return noteDao.getPinnedNotes().map { entities -> entities.map { it.toDomain() } }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun addNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
}

fun NoteEntity.toDomain() = Note(
    id = id,
    title = title,
    content = content,
    isPinned = isPinned,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    content = content,
    isPinned = isPinned,
    createdAt = createdAt,
    updatedAt = updatedAt
)
