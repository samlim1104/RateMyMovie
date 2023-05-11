package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating (
    var imbdbID: String ="tt2015381",
    var rating: Float = 4.5f,
    var ownerId: String? = null,
    var objectID: String? = null
        ):Parcelable