package com.example.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor


class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
         const val DATABASE_NAME = "user_data.db"
         const val DATABASE_VERSION = 1
         const val TABLE_NAME = "user"
         const val COL_ID = "id"
         const val COL_NAME = "name"
         const val COL_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT,
                $COL_EMAIL TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert user data into the database
    fun insertUserData(name: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_EMAIL, email)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Fetch user data
    fun getUserData(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }

}
