package com.example.notesapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.notesapp.LoginActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvUserName = view.findViewById(R.id.tvUserName)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Get the user's name from Google Sign-In
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        tvUserName.text = account?.displayName ?: "Guest"

        btnLogout.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun signOut() {
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {

                // Find ShowNotesFragment and clear notes
                /*val fragment = parentFragmentManager.findFragmentById(R.id.fragment_container) as? ShowNotesFragment
                fragment?.clearNotes()*/

                // Redirect to LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
    }
}
