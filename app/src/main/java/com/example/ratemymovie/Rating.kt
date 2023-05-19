package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating (
    var imbdID: String ="tt2015381",
    var movieName: String = "name",
    var rating: Float = 4.5f,
    var ownerId: String? = null,
    var objectId: String? = null
        ):Parcelable {

}