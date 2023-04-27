package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieData(
    var name: String = "name",
    var rating: Double = 4.5,
    var length: Int = 60,
) : Parcelable
