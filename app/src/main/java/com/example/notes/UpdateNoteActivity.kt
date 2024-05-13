package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.ActivityUpdateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId == -1) {
            finish()
            return
        }

        MainScope().launch {
            val note = db.getNoteByID(noteId)
            binding.updateTitleEditText.setText(note.title)
            binding.updateContentEditText.setText(note.content)
        }

        binding.saveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updateNote = Note(noteId, newTitle, newContent)

            // Launch a coroutine to update the note in the database asynchronously
            MainScope().launch(Dispatchers.IO) {
                db.updateNote(updateNote)
            }

            finish()
            Toast.makeText(this@UpdateNoteActivity, "Changes saved", Toast.LENGTH_SHORT).show()
        }
    }
}








