package com.example.ratemymovie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieData(
    var name: String = "name",
    var rating: Double = 4.5,
) : Parcelable
