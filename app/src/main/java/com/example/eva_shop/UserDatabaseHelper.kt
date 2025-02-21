package com.example.eva_shop

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_db"
        private const val DATABASE_VERSION = 1

        // Tabel pengguna
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Menambahkan pengguna baru
    fun addUser(username: String, password: String, name: String, email: String) {
        val db = writableDatabase
        val insertQuery = """
            INSERT INTO $TABLE_USERS ($COLUMN_USERNAME, $COLUMN_PASSWORD, $COLUMN_NAME, $COLUMN_EMAIL)
            VALUES ('$username', '$password', '$name', '$email')
        """
        db.execSQL(insertQuery)
        db.close()
    }

    // Mengecek apakah pengguna ada di database
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Mengubah password pengguna
    fun updatePassword(username: String, newPassword: String) {
        val db = writableDatabase
        val updateQuery = """
            UPDATE $TABLE_USERS SET $COLUMN_PASSWORD = ? WHERE $COLUMN_USERNAME = ?
        """
        val stmt = db.compileStatement(updateQuery)
        stmt.bindString(1, newPassword)
        stmt.bindString(2, username)
        stmt.executeUpdateDelete()
        db.close()
    }


}
