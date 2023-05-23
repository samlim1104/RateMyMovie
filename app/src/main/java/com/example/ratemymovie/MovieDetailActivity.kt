package com.example.ratemymovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.ratemymovie.databinding.ActivityMovieDetailBinding
import java.util.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    var MovieIsEditable = false
    lateinit var movie: MovieData
    lateinit var rating: Rating
    companion object {
        val EXTRA_MOVIE = "movie"
        val EXTRA_MATURITYRATING = "maturityRating"
        val EXTRA_YEAR = "year"
        val EXTRA_RUNTIME = "runtime"
        val EXTRA_GENRE = "genre"
        val EXTRA_PLOT = "plot"
        val EXTRA_RATING = "rating"
        val EXTRA_OWNERID = "ownerId"
        val EXTRA_OBJECTID = "objectId"
        val TAG = "MovieDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var passedMovie = intent.getParcelableExtra<MovieData>(EXTRA_MOVIE)
        var passedRating = intent.getParcelableExtra<Rating>(EXTRA_RATING)

        if(passedRating == null){
            rating = Rating()
            toggleEditable()
        } else {
            binding.ratingBarDetailRating.rating = (passedRating.rating)
        }
        if (passedMovie == null) {
            movie = MovieData("", "", 0, 0, "", "", 0F, "", "")
            toggleEditable()
        } else {
            movie = passedMovie!!
            binding.textViewDetailName.setText(passedMovie.name)
            binding.textView10DetailMatrat.setText(passedMovie.maturityRating)
            binding.textView11DetailRuntime.setText(passedMovie.runtime).toString()
            binding.textViewDetailGenre.setText(passedMovie.genre).toString()
            binding.textViewDetailPlot.setText(passedMovie.plot)
            binding.textViewDetailYear.setText(passedMovie.year).toString()
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
