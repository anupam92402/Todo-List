package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.modals.NotesModal

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note:NotesModal)

    @Update
    suspend fun update(note:NotesModal)

    @Delete
    suspend fun delete(note:NotesModal)

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes():LiveData<List<NotesModal>>

}