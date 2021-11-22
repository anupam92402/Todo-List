package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.modals.NotesModal
import kotlinx.android.synthetic.main.activity_add_edit_note_acitivity.*

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var viewModal: NoteViewModal
    var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note_acitivity)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        val noteType = intent.getStringExtra("type")
        if (noteType.equals("Edit")) {
            btnSave.text = "Update Todo"
            ETTitle.setText(intent.getStringExtra("title"))
            ETDescription.setText(intent.getStringExtra("description"))
            ETDate.setText(intent.getStringExtra("date"))
            ETTime.setText(intent.getStringExtra("time"))
            ETCategory.setText(intent.getStringExtra("category"))
            noteId = intent.getIntExtra("noteId", -1)
        } else {
            btnSave.text = "Create Todo"
        }

        btnSave.setOnClickListener {
            val noteTitle = ETTitle.text.toString()
            val noteDescription = ETDescription.text.toString()
            val noteDate = ETDate.text.toString()
            val noteTime = ETTime.text.toString()
            val noteCategory = ETCategory.text.toString()
            if (noteType.equals("Edit")) {
                val updateNote = NotesModal(
                    noteTitle,
                    noteDescription,
                    noteDate,
                    noteTime,
                    noteCategory
                )
                updateNote.id = noteId
                viewModal.updateNote(updateNote)
                Toast.makeText(this, "$noteTitle Updated", Toast.LENGTH_SHORT).show()
            } else {
                viewModal.addNote(
                    NotesModal(
                        noteTitle,
                        noteDescription,
                        noteDate,
                        noteTime,
                        noteCategory
                    )
                )
                Toast.makeText(this, "$noteTitle successfully added to the list", Toast.LENGTH_LONG)
                    .show()
            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }

    }

    private val timeInterval = 2000
    private var backPressed: Long = 0

    override fun onBackPressed() {
        if (backPressed + timeInterval > System.currentTimeMillis()) {
            startActivity(Intent(this, MainActivity::class.java))
            return
        } else {
            Toast.makeText(this, "Press again to go Back", Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }

}