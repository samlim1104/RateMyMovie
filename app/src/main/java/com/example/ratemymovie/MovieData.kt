package com.example.ratemymovie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieData(
    @SerializedName("Title")
    var name: String,
    @SerializedName("Rated")
    var rated: String?,
    @SerializedName("Year")
    var year: Int?,
    @SerializedName("Runtime")
    var runtime: String?,
    @SerializedName("Genre")
    var genre: String?,
    @SerializedName("Plot")
    var plot: String?,
    var rating: Float?,
    var ownerId: String?,
    var objectId: String?,

    ):Parcelable
