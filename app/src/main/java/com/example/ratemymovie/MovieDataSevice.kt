package com.example.ratemymovie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataSevice {
    @GET("t={title}.json")
    fun getMovieDataByTitle(@Path("title") name: String, @Query("apikey") apikey: String) : Call<MovieData>
}