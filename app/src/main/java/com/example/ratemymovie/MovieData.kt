package com.example.ratemymovie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieData(
    @SerializedName("Title")
    var name: String,
    var maturityRating: String,
    @SerializedName("Year")
    var year: Int,
    var runtime: Int,
    var genre: String,
    var plot: String,
    var rating: Float,
    var ownerId: String,
    var objectId: String,

):Parcelable
