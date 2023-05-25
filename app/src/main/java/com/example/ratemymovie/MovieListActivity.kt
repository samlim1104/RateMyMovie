package com.example.ratemymovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.example.ratemymovie.databinding.ActivityMovieListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMovieListBinding
    lateinit var adapter : MovieAdapter
    lateinit var userId : String
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
        // it's saying the userId is null..
        if(userId != null) {
            //retrieveAllData(userId!!)
        }

        binding.buttonSearch.setOnClickListener{
            var name = binding.editTextTextSearch.text

            val movieDataService = RetrofitHelper.getInstance().create(MovieDataSevice::class.java)
            val movieDataCall = movieDataService.getMovieDataByTitle(name.toString(), Constants.API_KEY)

            movieDataCall.enqueue(object: Callback<MovieWrapper>{
                override fun onResponse(
                    call: Call<MovieWrapper>,
                    response: Response<MovieWrapper>
                ) {
                    Log.d(TAG, "onResponse ${response.body()}\n${response.raw()}")

                    adapter = MovieAdapter(response.body())
                    binding.recyclerViewActivityMovielist.adapter = adapter
                    binding.recyclerViewActivityMovielist.layoutManager = LinearLayoutManager(this@MovieListActivity)
                }

                override fun onFailure(call: Call<MovieWrapper>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    override fun onStart() {
        super.onStart()
        userId = intent.getStringExtra(EXTRA_USER_ID).toString()
        if(userId != null) {
            //retrieveAllData(userId)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_log_reg ->{
                val loginIntent = Intent(this, LoginActivity :: class.java)
                startActivity(loginIntent)
                true
            }
            R.id.my_list ->{
                if(Backendless.UserService.CurrentUser() == null)
                {
                    val loginIntent = Intent(this, LoginActivity :: class.java)
                    startActivity(loginIntent)
                }
                else{
                    val listIntent = Intent(this, MyList::class.java)
                    startActivity(listIntent)
                }
                true
            }
            R.id.wanted_watch ->{
                if(Backendless.UserService.CurrentUser() == null)
                {
                    val loginIntent = Intent(this, LoginActivity :: class.java)
                    startActivity(loginIntent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*private fun retrieveAllData(userId : String) {
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
*/
}