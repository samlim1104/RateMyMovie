package com.example.ratemymovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.async.callback.Fault
import com.backendless.exceptions.BackendlessFault
import com.example.ratemymovie.databinding.ActivityMovieDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    var MovieIsEditable = false
    lateinit var movie: MovieData
    lateinit var rating: Rating
    companion object {
        val EXTRA_MOVIE = "movie"
        val EXTRA_RATING = "rating"
        val TAG = "MovieDetailActivity"
        val EXTRA_MOVIENAME = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var passedMovie = intent.getParcelableExtra<MovieData>(EXTRA_MOVIE)
        var passedRating = intent.getParcelableExtra<Rating>(EXTRA_RATING)
        Log.d(TAG, "onCreate: $passedMovie")

        if(passedRating == null){
            rating = Rating()
            toggleEditable()
        } else {
            binding.ratingBarDetailRating.rating = (passedRating.rating)
        }
        if (passedMovie == null) {
            movie = MovieData("", "", 0, "", "", "", 0F, "", "")
            toggleEditable()
        } else {
            movie = passedMovie!!
            val movieService = RetrofitHelper.getInstance().create(MovieDataSevice::class.java)
            val movieCall = movieService.getMovieDetailByTitle(movie.name, Constants.API_KEY)

            movieCall.enqueue(object: Callback<MovieData> {
                override fun onResponse(
                    call: Call<MovieData>,
                    response: Response<MovieData>
                ) {
                    Log.d(TAG, "onResponse ${response.body()}\n${response.raw()}")

                    binding.textViewDetailName.text = response.body()?.name
                    binding.textViewDetailGenre.text = response.body()?.genre
                    binding.textView10DetailMatrat.text = response.body()?.rated
                    binding.textViewDetailPlot.text = response.body()?.plot
                    binding.textView12DetailMin.text = response.body()?.runtime
                    binding.textViewDetailImdbid.text = response.body()?.objectId
                    binding.textViewDetailYear.text = response.body()?.year.toString()
                    binding.textView2Viewerrating.text = rating.rating.toString()


                }

                override fun onFailure(call: Call<MovieData>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }
//        binding.switchFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
//           if(isChecked){
//            rating.isFavorited = true
//           }
//        }
//        Log.d(TAG, "onCreate: rating: $rating")
        binding.buttonDetailSave.setOnClickListener {
            if(Backendless.UserService.CurrentUser() != null) {
                if (!rating.ownerId.isNullOrBlank()) {
//                rating.ownerId = intent.getStringExtra(MovieListActivity.EXTRA_USER_ID).toString()
                    rating.ownerId = Backendless.UserService.CurrentUser().objectId
                }

                rating.rating = binding.ratingBarDetailRating.rating
                rating.movieName = binding.textViewDetailName.text.toString()
                rating.imbdID = binding.textViewDetailImdbid.text.toString()
                if(binding.switchFavorite.isChecked)
                {
                    rating.favorited = true
                }
                if (rating.rating == null) {
                    Backendless.Data.of(Rating::class.java)
                        .save(rating, object : AsyncCallback<Rating> {
                            override fun handleResponse(response: Rating?) {
                                Log.d("hand", "handleResponse ${response}")
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d("fault", "handleFault ${fault!!.message}")
                            }
                        })
                    finish()
                } else {
                    Backendless.Data.of(Rating::class.java)
                        .remove(rating, object : AsyncCallback<Long?> {
                            override fun handleResponse(response: Long?) {
                                Toast.makeText(
                                    this@MovieDetailActivity,
                                    "${movie.name} Rating Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }

                            override fun handleFault(fault: BackendlessFault) {
                                Log.d("MovieDetail", "handleFault: ${fault.message}")
                            }
                        })
                    Backendless.Data.of(Rating::class.java)
                        .save(rating, object : AsyncCallback<Rating> {
                            override fun handleResponse(response: Rating?) {
                                Log.d("hand", "handleResponse ${response}")
                            }

                            override fun handleFault(fault: BackendlessFault?) {
                                Log.d("fault", "handleFault ${fault!!.message}")
                            }
                        })
                    finish()
                }
            }
            else{
                val loginIntent = Intent(this, LoginActivity :: class.java)
                startActivity(loginIntent)
            }
        }
    }
   //private fun retrieveAllData()



    private fun toggleEditable() {
        if (MovieIsEditable) {
            MovieIsEditable = false
            binding.buttonDetailSave.isEnabled = false
            binding.buttonDetailSave.visibility = View.GONE
            binding.textViewDetailName.inputType = InputType.TYPE_NULL
            binding.textViewDetailName.isEnabled = false

        } else {
            MovieIsEditable = false
            binding.buttonDetailSave.isEnabled = true
            binding.buttonDetailSave.visibility = View.VISIBLE
            binding.textViewDetailName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.textViewDetailName.isEnabled = true
        }
    }
}
