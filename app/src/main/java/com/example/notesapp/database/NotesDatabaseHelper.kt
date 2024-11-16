package com.example.notesapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.notesapp.model.Note

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 2
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
                userId TEXT
            )
        """
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN userId TEXT")
        }
    }

    fun insertNote(title: String, content: String, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
            put("userId", userId)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllNotes(userId: String): List<Note> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, "userId = ?",  arrayOf(userId), null, null, "$COLUMN_ID DESC")
        val notes = mutableListOf<Note>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val content = getString(getColumnIndexOrThrow(COLUMN_CONTENT))
                notes.add(Note(id, title, content))
            }
        }
        cursor.close()
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
