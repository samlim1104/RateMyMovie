package com.example.ratemymovie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(var movieList: MutableList<MovieData>):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewMovieName: TextView
        val textViewRating: TextView
        val layout: ConstraintLayout

        init {
            textViewMovieName = view.findViewById(R.id.textView_movie_name)
            textViewRating = view.findViewById(R.id.textView_movie_matrating)
            layout = view.findViewById(R.id.layout_movie)

        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_movie_data, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movieList[position]
        var context = holder.textViewMovieName.context
        holder.textViewMovieName.text = movie.name
        holder.textViewRating.text = movie.maturityRating.toString()

        holder.layout.setOnClickListener {
            val loanDetailActivity = Intent(it.context, MovieDetailActivity::class.java)
            loanDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
            it.context.startActivity(loanDetailActivity)
        }
    }
    /*private fun deleteFromBackendless(position: Int, con : Context) {
        Backendless.Data.of(MovieData::class.java).remove(movieList[position],
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    Toast.makeText(con, "${movieList[position].movieName} Deleted", Toast.LENGTH_SHORT).show()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    if (fault != null) {
                        Log.d("Didn't work", "handleFault: ${fault.message}")
                    }
                }
            })
    }
*/
    override fun getItemCount() = movieList.size

}