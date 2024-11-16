package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.notesapp.R
import com.example.notesapp.database.NotesDatabaseHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputEditText

class AddNewNoteFragment : Fragment() {

    private lateinit var dbHelper: NotesDatabaseHelper
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_note, container, false)

        dbHelper = NotesDatabaseHelper(requireContext())
        titleInput = view.findViewById(R.id.titleInput)
        descriptionInput = view.findViewById(R.id.descriptionInput)
        addButton = view.findViewById(R.id.readMoreButton)

        addButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            val userId = account?.id

            if (title.isNotEmpty() && description.isNotEmpty() && userId != null) {
                val id = dbHelper.insertNote(title, description, userId)
                if (id > 0) {
                    Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show()
                    titleInput.text?.clear()
                    descriptionInput.text?.clear()

                    // Navigate back to the ShowNotesFragment
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error adding note", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
