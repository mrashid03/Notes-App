package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NotesDatabaseHelper
import com.example.notesapp.model.Note
import com.google.android.gms.auth.api.signin.GoogleSignIn

class ShowNotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var dbHelper: NotesDatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        dbHelper = NotesDatabaseHelper(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadNotes()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        val userId = account?.id ?: ""

        if(userId != null) {
            val notesList = dbHelper.getAllNotes(userId).toMutableList() // Convert to MutableList
            adapter = NotesAdapter(notesList,
                onUpdateClicked = { note ->
                    val fragment = UpdateNoteFragment()
                    val args = Bundle()
                    args.putInt("noteId", note.id.toInt())
                    args.putString("noteTitle", note.title)
                    args.putString("noteContent", note.content)
                    fragment.arguments = args

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onDeleteClicked = { note ->
                    // Delete note from database
                    dbHelper.deleteNoteById(note.id)
                    // Remove note from adapter
                    adapter.removeNote(note)
                }
            )
            recyclerView.adapter = adapter
        }else{
            Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show()
        }
    }
}