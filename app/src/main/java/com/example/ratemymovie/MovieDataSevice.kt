package com.example.ratemymovie

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDataSevice {
    @GET(".")
    fun getMovieDataByTitle(@Query("s") name: String, @Query("apikey") apikey: String) : Call<MovieWrapper>

    @GET(".")
    fun getMovieDetailByTitle(@Query("t") names : String, @Query("apikey") apiKey: String) : Call<MovieData>
}

// baseUrl + get + ?s=name&apikey=blah
