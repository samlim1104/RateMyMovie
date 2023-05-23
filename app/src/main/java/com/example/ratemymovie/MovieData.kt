package com.example.ratemymovie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MovieData(
    @SerializedName("Title")
    var name: String,
    var Rated: String,
    @SerializedName("Year")
    var Year: Int,
    var Runtime: Int,
    var Genre: String,
    var Plot: String,
    var rating: Float,
    var ownerId: String,
    var objectId: String,

):Parcelable
