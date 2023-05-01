package com.example.ratemymovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.ratemymovie.databinding.ActivityMovieDetailBinding
import com.example.ratemymovie.databinding.ActivityMovieListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMovieListBinding
    lateinit var adapter : MovieAdapter
    companion object {
        val EXTRA_USER_ID = "userId"
        val TAG = "MovieListActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra(EXTRA_USER_ID)
        Log.d("onCreate", "$userId")
        retrieveAllData(userId!!)
        binding.recyclerViewActivityMovielist.setOnClickListener {
            val loanDetailIntent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
            startActivity(loanDetailIntent)
        }
    }
    override fun onStart() {
        super.onStart()
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        if(userId != null) {
            retrieveAllData(userId)
        }
    }

    private fun retrieveAllData(userId : String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(MovieData::class.java).find(queryBuilder, object :
            AsyncCallback<List<MovieData?>?> {
            override fun handleResponse(foundLoans: List<MovieData?>?) {
                Log.d(TAG, "handleResponse: $foundLoans")
                adapter = MovieAdapter(foundLoans as MutableList<MovieData>)
                binding.recyclerViewActivityMovielist.adapter = adapter
                binding.recyclerViewActivityMovielist.layoutManager =
                    LinearLayoutManager(this@MovieListActivity)
            }

            override fun handleFault(fault: BackendlessFault?) {
                if (fault != null) {
                    Log.d(TAG, "handleFault: ${fault.message})")
                }
            }
        })
    }

}