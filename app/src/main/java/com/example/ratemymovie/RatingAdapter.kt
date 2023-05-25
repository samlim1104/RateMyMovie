package com.example.ratemymovie

import android.content.Intent
import android.os.Build.VERSION_CODES.M
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RatingAdapter(var ratingList : MutableList<Rating>) : RecyclerView.Adapter<RatingAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewMovieName: TextView
        val textViewRating: TextView
        val layout: ConstraintLayout

        init {

            textViewMovieName = view.findViewById(R.id.textView_movie_name)
            textViewRating = view.findViewById(R.id.textView_movie_rating)
            layout = view.findViewById(R.id.layout_movie)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_movie_data, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingAdapter.ViewHolder, position: Int) {
        val rating = ratingList[position]
        val context = holder.textViewMovieName.context
        holder.textViewMovieName.text = rating.movieName
        holder.textViewRating.text = "PG"
        holder.layout.setOnClickListener{
            val detailIntent = Intent(it.context, MovieDetailActivity::class.java)
            detailIntent.putExtra(MovieDetailActivity.EXTRA_RATING, rating)
            it.context.startActivity(detailIntent)
        }

    }

    override fun getItemCount() = ratingList.size
}
