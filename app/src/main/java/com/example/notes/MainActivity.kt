package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db : NoteDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        notesAdapter = NotesAdapter(emptyList(), this,lifecycleScope)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // Use lifecycleScope to launch a coroutine and fetch notes from the database asynchronously
        lifecycleScope.launch(Dispatchers.Main) {
            val notes = db.getAllNotes()
            notesAdapter.refreshData(notes)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh notes when activity resumes
        lifecycleScope.launch {
            val notes = db.getAllNotes()
            notesAdapter.refreshData(notes)
        }
    }
}















