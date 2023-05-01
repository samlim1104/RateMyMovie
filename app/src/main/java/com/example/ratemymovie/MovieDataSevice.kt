package com.example.ratemymovie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataSevice {
    @GET("t.json")
    fun getMovieDataByTitle(@Path("title") name: String, @Query("apiKey") apiKey: String) : Call<List<MovieData>>

}