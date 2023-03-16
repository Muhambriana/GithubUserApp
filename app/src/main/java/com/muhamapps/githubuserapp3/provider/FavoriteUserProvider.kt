package com.muhamapps.githubuserapp3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.AUTHORITY
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.muhamapps.githubuserapp3.db.FavoriteUserHelper

class FavoriteUserProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_USER = 1
        private const val FAVORITE_USER_ID = 2
        private lateinit var favoriteUserHelper: FavoriteUserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE_USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", FAVORITE_USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteUserHelper = FavoriteUserHelper.getInstance(context as Context)
        favoriteUserHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE_USER -> favoriteUserHelper.queryAll()
            FAVORITE_USER_ID -> favoriteUserHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAVORITE_USER) {
            sUriMatcher.match(uri) -> favoriteUserHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (FAVORITE_USER_ID) {
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }


    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_USER_ID) {
            sUriMatcher.match(uri) -> favoriteUserHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
