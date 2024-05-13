package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notes.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        binding.saveButton.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val note = Note(0,title,content)

            // Launch a coroutine to insert the note into the database asynchronously
            MainScope().launch(Dispatchers.IO) {
                db.insertNote(note)
            }

            finish()
            Toast.makeText(this@AddNoteActivity, "Note Saved", Toast.LENGTH_SHORT).show()

//
//            db.insertNote(note)
//            finish()
//            Toast.makeText(this, "Note Saved",Toast.LENGTH_SHORT).show()
        }
    }
}