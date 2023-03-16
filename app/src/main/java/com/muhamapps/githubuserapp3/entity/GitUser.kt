package com.muhamapps.githubuserapp3.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GitUser (
    var username: String? = "",
    var name: String? = "",
    var location: String? = "",
    var repository: String? = "",
    var company: String? = "",
    var followers: String? = "",
    var following: String? = "",
    var avatar: String? = "",
    var status: Int = 0
): Parcelable
