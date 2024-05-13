package com.example.notes


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope



class NotesAdapter(private var notes: List<Note>, private val context: Context, private val coroutineScope: CoroutineScope) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val favoriteButton:ImageView=itemView.findViewById(R.id.favoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content


        // Set initial favorite button state based on note's favorite status
        if (note.isFavorite) {
            holder.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            holder.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        // Click listener for favorite button
        holder.favoriteButton.setOnClickListener {
            // Toggle favorite status in database
            coroutineScope.launch(Dispatchers.IO) {
                note.isFavorite = !note.isFavorite
                db.updateNote(note)

                // Retrieve updated notes from the database
                val newNotes = db.getAllNotes()

                // Switch to the main thread to update the UI
                launch(Dispatchers.Main) {
                    // Update the list in the adapter
                    refreshData(newNotes)

                    // Show a toast to indicate the favorite status has been updated
                    val message = if (note.isFavorite) "Added to Favorites" else "Removed from Favorites"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            coroutineScope.launch(Dispatchers.IO) {
                // Delete the note
                db.deleteNote(note.id)

                // Retrieve updated notes from the database
                val newNotes = db.getAllNotes()

                // Switch to the main thread to update the UI
                launch(Dispatchers.Main) {
                    // Update the list in the adapter
                    refreshData(newNotes)

                    // Show a toast to indicate the note has been deleted
                    Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}

