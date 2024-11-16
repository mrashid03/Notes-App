package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.model.Note

class NotesAdapter(
    private val notes: MutableList<Note>,
    private val onUpdateClicked: (Note) -> Unit,
    private val onDeleteClicked: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note){
            val titleView = itemView.findViewById<TextView>(R.id.noteTitle)
            val dateView = itemView.findViewById<TextView>(R.id.date)
            val deleteButton = itemView.findViewById<ImageButton>(R.id.notesDeleteBtn)

            titleView.text = note.title
            dateView.text = note.date

            titleView.setOnClickListener {
                onUpdateClicked(note)
            }

            deleteButton.setOnClickListener {
                onDeleteClicked(note) // Trigger the delete callback
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // Method to remove a note from the list and notify adapter
    fun removeNote(note: Note) {
        val position = notes.indexOf(note)
        if (position != -1) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}