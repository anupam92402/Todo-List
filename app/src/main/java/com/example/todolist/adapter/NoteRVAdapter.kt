package com.example.todolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.modals.NotesModal

class NoteRVAdapter(
    private val noteClickedInterface: NoteClickedInterface,
    private val context:Context) :
    RecyclerView.Adapter<NoteRVAdapter.NoteViewHolder>() {

     val allNotes = ArrayList<NotesModal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_rv_item,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.noteTitleTV.text = allNotes[position].noteTitle
        holder.noteDescriptionTV.text = allNotes[position].noteDescription
        holder.noteDateTV.text = allNotes[position].noteDate
        holder.noteTimeTV.text = allNotes[position].noteTime
        holder.noteCategoryTV.text = allNotes[position].noteCategory
        val colors = context.resources.getIntArray(R.array.random_color)
        holder.noteCardView.setCardBackgroundColor(colors[position%10])
        holder.noteMoreIV.setOnClickListener{
           noteClickedInterface.onMoreIconClick(allNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitleTV: TextView = itemView.findViewById(R.id.idTVNoteTitle)
        val noteDescriptionTV: TextView = itemView.findViewById(R.id.idTVNoteDescription)
        val noteDateTV: TextView = itemView.findViewById(R.id.idTVNoteDate)
        val noteTimeTV: TextView = itemView.findViewById(R.id.idTVNoteTime)
        val noteCategoryTV: TextView = itemView.findViewById(R.id.idTVNoteCategory)
        val noteMoreIV: ImageView = itemView.findViewById(R.id.idIVNoteMore)
        val noteCardView:CardView = itemView.findViewById(R.id.idRVNoteCardView)
    }

    fun updateList(newNoteList:List<NotesModal>){
        allNotes.clear()
        allNotes.addAll(newNoteList)
        notifyDataSetChanged()
    }

    fun deleteFromList(position: Int):NotesModal{
        return allNotes[position]
    }

}

interface NoteClickedInterface {
    fun onMoreIconClick(note: NotesModal)
}