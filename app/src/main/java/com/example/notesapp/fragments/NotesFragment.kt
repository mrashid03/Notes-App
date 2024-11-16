package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        if (userId.isNotEmpty()) {
            val notesList = dbHelper.getAllNotes(userId).toMutableList() // Convert to MutableList
            adapter = NotesAdapter(notesList,
                onUpdateClicked = { note ->
                    val fragment = UpdateNoteFragment()
                    val args = Bundle().apply {
                        putInt("noteId", note.id.toInt())
                        putString("noteTitle", note.title)
                        putString("noteContent", note.content)
                        putString("noteDate", note.date)
                    }
                    fragment.arguments = args

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onDeleteClicked = { note ->
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes") { dialog, _ ->
                            // Delete note from database
                            dbHelper.deleteNoteById(note.id)

                            // Remove note from adapter
                            adapter.removeNote(note)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            )
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show()
        }
    }
}