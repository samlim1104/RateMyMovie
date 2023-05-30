package com.example.ratemymovie

import android.content.Intent
import android.graphics.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.ratemymovie.databinding.ActivityMyListBinding

class MyList : AppCompatActivity() {
    companion object{
        val EXTRA_OBJECT_ID = "objectId"
        val EXTRA_USER_ID = "userId"
        val TAG = "MyList"
    }
    private lateinit var binding: ActivityMyListBinding
    private lateinit var adapter: RatingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val objectId = intent.getStringExtra(
//            EXTRA_OBJECT_ID)
        val userId =  Backendless.UserService.CurrentUser().userId
        Log.d("onCreate", "$userId")
        retrieveAllData(userId!!)
    }

    private fun retrieveAllData(userId:String){
        val whereClause = "ownerId =  '$userId' AND favorited = TRUE"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(Rating::class.java).find(queryBuilder, object :
            AsyncCallback<List<Rating?>?> {
            override fun handleResponse(foundRatings: List<Rating?>?) {
                Log.d(MovieDetailActivity.TAG, "hi : $foundRatings")
                adapter = RatingAdapter(foundRatings as MutableList<Rating>)
                binding.recyclerMyList.adapter = adapter
                binding.recyclerMyList.layoutManager = LinearLayoutManager(this@MyList)
            }

            override fun handleFault(fault: BackendlessFault) {
                Log.d(LoginActivity.TAG, "handleFault: ${fault.message}")
            }
        })
    }
}