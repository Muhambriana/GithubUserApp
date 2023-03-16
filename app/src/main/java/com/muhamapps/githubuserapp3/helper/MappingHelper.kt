package com.muhamapps.githubuserapp3.helper

import android.database.Cursor
import com.muhamapps.githubuserapp3.db.FavoriteUserContract
import com.muhamapps.githubuserapp3.entity.GitUser


object MappingHelper {

    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<GitUser> {
        val gitUser = ArrayList<GitUser>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_USERNAME))
                val name = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_NAME))
                val location = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_LOCATION))
                val repository = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_REPOSITORY))
                val company = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_AVATAR))
                gitUser.add(GitUser(username, name, location,repository, company,followers, following, avatar))
            }
        }
        return gitUser
    }

    fun mapCursorToObject(cursor: Cursor?): GitUser? {
        var gitUser: GitUser? = null
        if (cursor != null) {
            if (cursor.moveToNext()){
                cursor.apply {
                    val username =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_USERNAME))
                    val name =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_NAME))
                    val location =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_LOCATION))
                    val repository =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_REPOSITORY))
                    val company =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_COMPANY))
                    val followers =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWERS))
                    val following =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWING))
                    val avatar =
                        getString(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_AVATAR))
                    val status =
                        getInt(getColumnIndexOrThrow(FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_STATUS))
                    gitUser = GitUser(
                        username,
                        name,
                        location,
                        repository,
                        company,
                        followers,
                        following,
                        avatar,
                        status
                    )
                }
            }
        }
        return gitUser
    }
}