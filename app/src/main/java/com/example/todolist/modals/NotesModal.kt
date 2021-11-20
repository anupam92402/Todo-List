package com.example.todolist.modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class NotesModal(
    @ColumnInfo(name = "title") val noteTitle: String,
    @ColumnInfo(name = "description") val noteDescription: String,
    @ColumnInfo(name = "date") val noteDate: String,
    @ColumnInfo(name = "time") val noteTime: String,
    @ColumnInfo(name = "category") val noteCategory: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}