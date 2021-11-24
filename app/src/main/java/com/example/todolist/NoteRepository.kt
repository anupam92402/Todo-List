package com.example.todolist

import androidx.lifecycle.LiveData
import com.example.todolist.modals.NotesModal

class NoteRepository(private val notesDao: NotesDao) {

    val allNotes: LiveData<List<NotesModal>> = notesDao.getAllNotes()

    suspend fun insert(note: NotesModal) {
        notesDao.insert(note)
    }

    suspend fun delete(note: NotesModal) {
        notesDao.delete(note)
    }

    suspend fun update(note: NotesModal) {
        notesDao.update(note)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<NotesModal>> {
        return notesDao.searchDatabase(searchQuery)
    }

}