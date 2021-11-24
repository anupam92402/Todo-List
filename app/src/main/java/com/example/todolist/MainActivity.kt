package com.example.todolist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import java.util.*

class MainActivity : AppCompatActivity(), NoteClickedInterface,
   SearchView.OnQueryTextListener {

    lateinit var viewModal: NoteViewModal
    lateinit var noteRVAdapter: NoteRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting tbe recycler view
        idRVNotes.layoutManager = LinearLayoutManager(this)
        noteRVAdapter = NoteRVAdapter(this, this)
        idRVNotes.adapter = noteRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        //using observer
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                if (it.isEmpty()) {
                    idIVNote.visibility = View.VISIBLE
                    idTVNoteMessage.visibility = View.VISIBLE
                } else {
                    idIVNote.visibility = View.INVISIBLE
                    idTVNoteMessage.visibility = View.INVISIBLE
                }
                noteRVAdapter.updateList(it)
            }
        })

        //swipe to refresh
        swipeToRefresh.setOnRefreshListener {
            viewModal.allNotes.observe(this, Observer { list ->
                list?.let {
                    noteRVAdapter.updateList(it)
                }
            })
            swipeToRefresh.isRefreshing = false
        }

        //send to create new notes activity
        idFABAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra("type", "Create")
            startActivity(intent)
            this.finish()
        }

        //setting up swipe gesture
        val swipeGesture = object : RVSwipeGestures(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position: Int = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val item = noteRVAdapter.deleteFromList(position)
                        viewModal.deleteNote(item)
                        Snackbar.make(
                            idRVNotes,
                            "${item.noteTitle} Task Deleted",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Undo", View.OnClickListener {
                                viewModal.addNote(item)
                                Toast.makeText(
                                    this@MainActivity, "Reinserted ${item.noteTitle} Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }).show()

                    }
                    ItemTouchHelper.RIGHT -> {
                        val item = noteRVAdapter.deleteFromList(position)
                        viewModal.deleteNote(item)
                        Snackbar.make(
                            idRVNotes,
                            "${item.noteTitle} Task Completed",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("Undo", View.OnClickListener {
                                viewModal.addNote(item)
                                Toast.makeText(
                                    this@MainActivity, "Reinserted ${item.noteTitle} Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }).show()

                    }
                }
            }
        }

        //attaching swipe gesture to recycler view
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(idRVNotes)

    }

    override fun onMoreIconClick(note: NotesModal) {
        showDialog(note)
    }

    private fun showDialog(note: NotesModal) {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.more_bottom_sheet)

        val viewLinearLayout = dialog.findViewById<LinearLayout>(R.id.viewLinearLayout)
        val editLinearLayout = dialog.findViewById<LinearLayout>(R.id.editLinearLayout)
        val shareLinearLayout = dialog.findViewById<LinearLayout>(R.id.shareLinearLayout)

        viewLinearLayout.setOnClickListener {
            val intent = Intent(this, viewNoteOnlyActivity::class.java)
            startActivity(intent)
        }
        editLinearLayout.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra("type", "Edit")
            intent.putExtra("title", note.noteTitle)
            intent.putExtra("description", note.noteDescription)
            intent.putExtra("date", note.noteDate)
            intent.putExtra("time", note.noteTime)
            intent.putExtra("category", note.noteCategory)
            intent.putExtra("noteId", note.id)
            startActivity(intent)
            this.finish()
        }
        shareLinearLayout.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, note.noteTitle + "\n" + note.noteDescription)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "$query%"

        viewModal.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                noteRVAdapter.updateList(it)
            }
        })
    }

}

