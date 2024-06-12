package com.example.mynotesmvc

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private val notes = ArrayList<Note>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var noteInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteInput = findViewById(R.id.noteInput)
        val noteList: ListView = findViewById(R.id.noteList)
        val addButton: Button = findViewById(R.id.addButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        noteList.adapter = adapter

        loadNotes()

        addButton.setOnClickListener {
            val noteContent = noteInput.text.toString()
            if (noteContent.isNotEmpty()) {
                val newNote = Note(noteContent)
                notes.add(newNote)
                adapter.add(newNote.content)
                noteInput.setText("")
                saveNotes()
            } else {
                Log.e("MainActivity", "Note content is empty")
            }
        }
    }

    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("SimpleNotesMVC", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(notes)
        editor.putString("notes", json)
        editor.apply()
        Log.d("MainActivity", "Notes saved")
    }

    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("SimpleNotesMVC", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("notes", null)
        val type = object : TypeToken<ArrayList<Note>>() {}.type
        if (json != null) {
            val loadedNotes: ArrayList<Note> = gson.fromJson(json, type)
            notes.addAll(loadedNotes)
            for (note in loadedNotes) {
                adapter.add(note.content)
            }
            Log.d("MainActivity", "Notes loaded")
        } else {
            Log.d("MainActivity", "No notes found")
        }
    }
}
