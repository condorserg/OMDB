package com.condorserg.omdb.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.condorserg.omdb.R
import com.condorserg.omdb.RemoteMovie
import com.condorserg.omdb.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MovieListAdapterDelegate: AbsListItemAdapterDelegate<RemoteMovie, RemoteMovie, MovieListAdapterDelegate.MoviesHolder>() {


    class MoviesHolder(
            view: View
    ) : RecyclerView.ViewHolder(view) {

        private val movieTitleTextView: TextView = view.findViewById(R.id.movieTitleTextView)
        private val movieYearTextView: TextView = view.findViewById(R.id.movieYearTextView)
        private val movieIDTextView: TextView = view.findViewById(R.id.imdbIDTextView)
        private val movieImageView: ImageView = view.findViewById(R.id.movieImageView)

        fun bind(movie: RemoteMovie) {
            movieTitleTextView.text = movie.title
            movieYearTextView.text = movie.year
            movieIDTextView.text = movie.imdbID
            Glide.with(itemView)
                    .load(movie.poster)
                    .into(movieImageView)
        }
    }

    override fun isForViewType(item: RemoteMovie, items: MutableList<RemoteMovie>, position: Int): Boolean {
        return true

    }

    override fun onCreateViewHolder(parent: ViewGroup): MoviesHolder {
               return MoviesHolder(
                parent.inflate(R.layout.item_movie))
    }

    override fun onBindViewHolder(item: RemoteMovie, holder: MoviesHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

}