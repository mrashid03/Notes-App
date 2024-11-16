package com.example.notesapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log
import com.example.notesapp.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 3
        const val TABLE_NAME = "notes"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_CONTENT TEXT,
                userId TEXT,
                date TEXT
            )
        """
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN userId TEXT")
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN date TEXT")
        }
    }

    fun insertNote(title: String, content: String, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
            put("userId", userId)
            put("date", SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()))
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllNotes(userId: String): List<Note> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, "userId = ?",  arrayOf(userId), null, null, "$COLUMN_ID DESC")
        val notes = mutableListOf<Note>()
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                    val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    val date = it.getString(it.getColumnIndexOrThrow("date")) // Assuming date column is now added
                    notes.add(Note(id, title, content, date)) // Add the date to the Note object
                } while (it.moveToNext())
            }
            it.close()
        } 

        return notes
    }

    fun updateNote(id: Int, title: String, content: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("title", title)
        values.put("content", content)
        db.update("notes", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteNoteById(noteId: Long) {
        val db = this.writableDatabase
        db.delete("Notes", "id=?", arrayOf(noteId.toString()))
        db.close()
    }
}
