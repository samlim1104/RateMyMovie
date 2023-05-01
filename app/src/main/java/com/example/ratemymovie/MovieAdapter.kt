package com.example.ratemymovie

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault

class MovieAdapter(var loanList: MutableList<MovieData>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
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
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loan = loanList[position]
        var context = holder.textViewBorrower.context
        holder.textViewBorrower.text = loan.lendee
        holder.textViewAmountOwed.text = String.format("$%.2f", loan.initialLoanValue - loan.amountRepaid)

        holder.layout.setOnClickListener {
            val loanDetailActivity = Intent(it.context, MovieDetailActivity::class.java)
            loanDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, loan)
            it.context.startActivity(loanDetailActivity)
        }
        holder.layout.isLongClickable = true
        holder.layout.setOnLongClickListener {
            val popMenu = PopupMenu(context, holder.textViewBorrower)
            popMenu.inflate(R.menu.menu_loanlist_context)
            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_loan_detail_delete -> {
                        deleteFromBackendless(position, context)
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
            popMenu.show()
            true
        }
    }
    private fun deleteFromBackendless(position: Int, con : Context) {
        Backendless.Data.of(MovieData::class.java).remove(loanList[position],
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    Toast.makeText(con, "${loanList[position].name} Deleted", Toast.LENGTH_SHORT).show()
                }

                override fun handleFault(fault: BackendlessFault?) {
                    if (fault != null) {
                        Log.d("Didn't work", "handleFault: ${fault.message}")
                    }
                }
            })
    }

    override fun getItemCount() = loanList.size
}