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

    companion object {
        val EXTRA_MOVIE = "movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var passedMovie = intent.getParcelableExtra<MovieData>(EXTRA_MOVIE)
        if (passedMovie == null) {
            movie = MovieData()
            toggleEditable()
        } else {
            movie = passedMovie!!
            binding.editTextMovieName.setText(movie.name)
            binding.editTextRating.setText(movie.rating.toString())
        }
            binding.buttonSave.setOnClickListener {
                movie.name = binding.editTextMovieName.text.toString()
                movie.rating = binding.editTextRating.text.toString().toDouble()
                Backendless.Data.of(MovieData::class.java)
                    .save(movie, object : AsyncCallback<MovieData> {
                        override fun handleResponse(response: MovieData?) {
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
            binding.buttonSave.isEnabled = false
            binding.buttonSave.visibility = View.GONE
            binding.editTextMovieName.inputType = InputType.TYPE_NULL
            binding.editTextMovieName.isEnabled = false
            binding.editTextRating.inputType = InputType.TYPE_NULL
            binding.editTextRating.isEnabled = false
        } else {
            MovieIsEditable = false
            binding.buttonSave.isEnabled = true
            binding.buttonSave.visibility = View.VISIBLE
            binding.editTextMovieName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.editTextMovieName.isEnabled = true
            binding.editTextRating.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextRating.isEnabled = true
        }
    }
}
