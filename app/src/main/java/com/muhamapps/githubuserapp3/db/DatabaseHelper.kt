package com.muhamapps.githubuserapp3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_git_user"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE_GIT_USER = "CREATE TABLE $TABLE_NAME" +
                " (${FavoriteUserColumns.COLUMN_NAME_USERNAME} TEXT NOT NULL PRIMARY KEY," +
                " ${FavoriteUserColumns.COLUMN_NAME_NAME} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_LOCATION} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_REPOSITORY} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_COMPANY} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_FOLLOWERS} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_FOLLOWING} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_AVATAR} TEXT NOT NULL," +
                " ${FavoriteUserColumns.COLUMN_NAME_STATUS} INTEGER NOT NULL DEFAULT '0')"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_GIT_USER)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}