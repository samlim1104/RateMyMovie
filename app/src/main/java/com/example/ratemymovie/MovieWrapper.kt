package com.example.ratemymovie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieWrapper (
    @SerializedName("Search")
    var results : List<MovieData>?
) : Parcelable