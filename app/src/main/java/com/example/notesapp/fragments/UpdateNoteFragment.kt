package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.notesapp.R
import com.example.notesapp.database.NotesDatabaseHelper

class UpdateNoteFragment : Fragment() {

    private lateinit var dbHelper: NotesDatabaseHelper
    private var noteId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_note, container, false)
        dbHelper = NotesDatabaseHelper(requireContext())

        // Retrieve arguments (note ID, title, content)
        val args = arguments
        noteId = args?.getInt("noteId")?.toLong() ?: -1L
        val noteTitle = args?.getString("noteTitle") ?: ""
        val noteContent = args?.getString("noteContent") ?: ""

        // Populate UI with existing note details
        val editNoteTitle = view.findViewById<EditText>(R.id.editNoteTitle)
        val editNoteContent = view.findViewById<EditText>(R.id.editNoteContent)
        editNoteTitle.setText(noteTitle)
        editNoteContent.setText(noteContent)

        // Save updated note
        val saveButton = view.findViewById<Button>(R.id.saveNoteButton)
        saveButton.setOnClickListener {
            val updatedTitle = editNoteTitle.text.toString()
            val updatedContent = editNoteContent.text.toString()
            dbHelper.updateNote(noteId.toInt(), updatedTitle, updatedContent)

            // Return to the previous fragment
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
