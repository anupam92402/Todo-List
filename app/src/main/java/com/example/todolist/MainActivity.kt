package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.adapter.NoteClickedInterface
import com.example.todolist.adapter.NoteRVAdapter
import com.example.todolist.modals.NotesModal
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NoteClickedInterface {

    lateinit var viewModal: NoteViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting tbe recycler view
        idRVNotes.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteRVAdapter(this, this)
        idRVNotes.adapter = noteRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        //using observer
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                if(noteRVAdapter.allNotes.isEmpty()){
                    idIVNote.visibility = View.VISIBLE
                    idTVNoteMessage.visibility=View.VISIBLE
                }else{
                    idIVNote.visibility = View.INVISIBLE
                    idTVNoteMessage.visibility=View.INVISIBLE
                }
                noteRVAdapter.updateList(it)
            }
        })

        //send to create new notes activity
        idFABAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        //setting up swipe gesture
        val swipegesture = object : RVSwipeGestures(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position: Int = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val item = noteRVAdapter.deleteFromList(position)
                        viewModal.deleteNote(item)
                        Snackbar.make(idRVNotes,"${item.noteTitle} Deleted",Snackbar.LENGTH_SHORT)
                            .setAction("Undo",View.OnClickListener {
                               viewModal.addNote(item)
                                Toast.makeText(this@MainActivity,"Reinserted Successfully",
                                    Toast.LENGTH_SHORT).show()
                            }) .show()

                    }
                    ItemTouchHelper.RIGHT -> {
                        val item = noteRVAdapter.deleteFromList(position)
                        viewModal.deleteNote(item)
                        Snackbar.make(idRVNotes,"${item.noteTitle} Completed",Snackbar.LENGTH_SHORT)
                            .setAction("Undo",View.OnClickListener {
                                viewModal.addNote(item)
                                Toast.makeText(this@MainActivity,"Reinserted Successfully",
                                    Toast.LENGTH_SHORT).show()
                            }) .show()

                    }
                }
            }
        }

        //attaching swipe gesture to recycler view
        val itemTouchHelper = ItemTouchHelper(swipegesture)
        itemTouchHelper.attachToRecyclerView(idRVNotes)

    }

    override fun onMoreIconClick(note: NotesModal) {
        Toast.makeText(this, "More Options Clicked", Toast.LENGTH_LONG).show()
    }
}

