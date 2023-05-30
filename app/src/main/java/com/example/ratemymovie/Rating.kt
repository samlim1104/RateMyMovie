package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating (
    var imbdID: String = "tt2015381",
    var movieName: String = "name",
    var rating: Float = 0f,
    var ownerId: String? = null,
    var objectId: String? = null,
    var favorited: Boolean = false
        ):Parcelable {

}