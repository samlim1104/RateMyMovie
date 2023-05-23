package com.example.ratemymovie

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

                }

                override fun onFailure(call: Call<MovieData>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
            binding.textViewDetailName.setText(passedMovie.name)
            binding.textView10DetailMatrat.setText(passedMovie.rated)
            //binding.textView11DetailRuntime.setText(passedMovie.runtime).toString()
            binding.textViewDetailGenre.setText(passedMovie.genre).toString()
            binding.textViewDetailPlot.setText(passedMovie.plot)
            //binding.textViewDetailYear.setText(passedMovie.year!!).toString()
        }
        Log.d(TAG, "onCreate: rating: $rating")
        binding.buttonDetailSave.setOnClickListener {
            if(rating.ownerId.isNullOrBlank()){
                Log.d(TAG, "onCreate userId: ${intent.getStringExtra(MovieListActivity.EXTRA_USER_ID)}")
                rating.ownerId = intent.getStringExtra(MovieListActivity.EXTRA_USER_ID)!!
            }
            rating.rating = binding.ratingBarDetailRating.rating
            rating.imbdID = binding.textViewDetailImdbid.text.toString()
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
    private fun deleteFromBackendless() {
        Backendless.Data.of(MovieData::class.java).remove(movie,
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
// Person has been deleted. The response is the
// time in milliseconds when the object was deleted
                    Toast.makeText(
                        this@MovieDetailActivity,
                        "${movie.name} Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d("BirthdayDetail", "handleFault: ${fault.message}")
                }
            })
    }



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
