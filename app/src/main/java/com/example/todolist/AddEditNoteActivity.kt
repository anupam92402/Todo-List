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

        btnSave.setOnClickListener {
            val noteTitle =ETTilte.text.toString()
            val noteDescription =ETDescription.text.toString()
            val noteDate =ETDate.text.toString()
            val noteTime =ETTime.text.toString()
            val noteCategory =ETCategory.text.toString()

            viewModal.addNote(NotesModal(noteTitle,noteDescription,noteDate,noteTime,noteCategory))
            Toast.makeText(this, "$noteTitle successfully added to list",Toast.LENGTH_LONG).show()
            startActivity(Intent(applicationContext,MainActivity::class.java))
            this.finish()
        }

    }

    private val timeInterval = 2000
    private var backPressed:Long =0

    override fun onBackPressed() {
        if(backPressed+timeInterval>System.currentTimeMillis()) {
            startActivity(Intent(this,MainActivity::class.java))
            return
        }else{
            Toast.makeText(this,"Tap again to Exit",Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }

}