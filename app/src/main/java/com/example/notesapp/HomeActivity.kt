package com.example.notesapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.notesapp.fragments.AddNewNoteFragment
import com.example.notesapp.fragments.ProfileFragment
import com.example.notesapp.fragments.ShowNotesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        replaceFrameWithFragment(ShowNotesFragment())

        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.homeBtn -> {
                    replaceFrameWithFragment(ShowNotesFragment())
                    true
                }
                R.id.addBtn -> {
                    replaceFrameWithFragment(AddNewNoteFragment())
                    true
                }
                R.id.profileBtn -> {
                    replaceFrameWithFragment(ProfileFragment())
                    true
                }
                else -> false
            }

        }

    }

    private fun replaceFrameWithFragment(frag: Fragment) {

        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragment_container, frag)
        fragTransaction.commit()

    }
}
