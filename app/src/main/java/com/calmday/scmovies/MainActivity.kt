package com.calmday.scmovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class MainActivity : AppCompatActivity() {
    val client = AsyncHttpClient()
    var movies = mutableListOf<Movie>()
    var movieAdapter: MovieAdapter = MovieAdapter(movies)
    lateinit var r_movies: RecyclerView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        r_movies = findViewById(R.id.movies)
        movies()
        r_movies.adapter = movieAdapter
        r_movies.layoutManager = LinearLayoutManager(this)

    }


    fun movies(): List<Movie> {
        val params = RequestParams()
        params["api_key"] = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
        client["https://api.themoviedb.org/3/movie/now_playing", params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.v("api call", json.toString())
                if (json != null && json.jsonObject != null) {
                    val response = json!!.jsonObject
                    val moves = response.getJSONArray("results")

                    for (i in 0 until moves.length()) {
                        val m = moves.getJSONObject(i)

                        var mm = Movie(m.getString("original_title"), m.getString("poster_path"), m.getString("overview"))

                        movies.add(mm)
                    }
                }
                movieAdapter.replaceList(movies)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String?,
                t: Throwable?
            ) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        }]
        return emptyList()
    }
}


// new since Glide v4
@GlideModule
class MovieGlide : AppGlideModule() {
    // leave empty for now
}

class Movie(val name: String, val image: String, val overview: String);

class MovieAdapter(var movies: MutableList<Movie>): RecyclerView.Adapter<MovieAdapter.ViewHolder> () {
    inner class ViewHolder(movie: View): RecyclerView.ViewHolder(movie) {
        val image = movie.findViewById<ImageView>(R.id.poster)
        val name = movie.findViewById<TextView>(R.id.name)
        val overview = movie.findViewById<TextView>(R.id.overview)
    }

    public fun replaceList(movies: MutableList<Movie>) {
        this.movies = movies;
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val movieView = inflater.inflate(R.layout.movie, parent, false)
        // Return a new holder instance
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: Movie = movies.get(position)
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500/" + movie.image)
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.imagenotfound)
            .into(holder.image);
        holder.name.text = movie.name
        holder.overview.text = movie.overview

    }

    override fun getItemCount(): Int {
        return movies.size;
    }
}