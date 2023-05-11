package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieData(
    var name: String = "name",
    var maturityRating: String = "PG",
    var year: Int = 1996,
    var runtime: Int = 121,
    var genre: String = "genre",
    var plot: String = "plot",

):Parcelable
