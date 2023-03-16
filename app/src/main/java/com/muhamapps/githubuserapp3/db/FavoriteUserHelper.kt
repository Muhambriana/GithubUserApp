package com.muhamapps.githubuserapp3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.COLUMN_NAME_USERNAME
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.TABLE_NAME

class FavoriteUserHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteUserHelper? = null

        fun getInstance(context: Context): FavoriteUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteUserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_NAME_USERNAME ASC"
        )
    }

    fun queryById(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "username = '$username'",
            null,
            null,
            null,
            null)
    }

    fun deleteById(username: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERNAME = '$username'", null)
    }


}