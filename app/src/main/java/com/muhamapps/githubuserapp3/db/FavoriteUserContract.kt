package com.muhamapps.githubuserapp3.db

import android.net.Uri
import android.provider.BaseColumns

object FavoriteUserContract {

    const val AUTHORITY = "com.muhamapps.githubuserapp3"
    const val SCHEME = "content"

    internal class FavoriteUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_git_user"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_LOCATION = "location"
            const val COLUMN_NAME_REPOSITORY = "repository"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_FOLLOWERS = "followers"
            const val COLUMN_NAME_FOLLOWING = "following"
            const val COLUMN_NAME_AVATAR = "avatar"
            const val COLUMN_NAME_STATUS = "status"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

}