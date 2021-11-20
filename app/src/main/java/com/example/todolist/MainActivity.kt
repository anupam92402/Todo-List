package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.adapter.NoteClickedInterface
import com.example.todolist.adapter.NoteRVAdapter
import com.example.todolist.modals.NotesModal
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NoteClickedInterface {

    lateinit var viewModal: NoteViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        idRVNotes.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteRVAdapter(this, this)
        idRVNotes.adapter = noteRVAdapter
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        })

        idFABAddNote.setOnClickListener {
            val intent = Intent(this,AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }



    }



    override fun onMoreIconClick(note: NotesModal) {
        Toast.makeText(this, "More Options Clicked", Toast.LENGTH_LONG).show()
    }
}

