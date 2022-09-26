package com.calmday.scmovies

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import java.lang.Exception

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
//        val intent = Intent(this, TVPage::class.java)
//        startActivity(intent)
    }


    fun movies(): List<Movie> {
        val params = RequestParams()
        params["api_key"] = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
        client["https://api.themoviedb.org/3/tv/popular", params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                Log.v("api call", json.toString())
                if (json != null && json.jsonObject != null) {
                    val response = json!!.jsonObject
                    val moves = response.getJSONArray("results")

                    for (i in 0 until moves.length()) {
                        val m = moves.getJSONObject(i)
                        var t = "q"
                        var p = "e"
                        var o = "r"
                        var d = "t"
                        var pp = 0.0
                        var va = 0.0
                        try {
                            val op = m.getString("original_name")
                            t = op
                        } catch (e: Exception) {

                        }
                        try {
                            val op = m.getString("poster_path")
                            p = op
                        } catch (e: Exception) {

                        }
                        try {
                            val op = m.getString("overview")
                            o = op
                        } catch (e: Exception) {

                        }
                        try {
                            val op = m.getString("first_air_date")
                            d = op
                        } catch (e: Exception) {

                        }
                        try {
                            val op = m.getDouble("popularity")
                            pp = op
                        } catch (e: Exception) {

                        }
                        try {
                            val op = m.getDouble("vote_average")
                            va = op
                        } catch (e: Exception) {

                        }
                        var mm = Movie(t, p, o, d, pp, va)

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
                Log.v("api call error", errorResponse ?: "unknown error sam")
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

class Movie(val name: String, val image: String, val overview: String, val date: String, val popularity: Double, val vote_average: Double
)

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
        val movieView = inflater.inflate(R.layout.tv_show, parent, false)
        // Return a new holder instance
        return ViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: Movie = movies.get(position)
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500/" + movie.image)
            .transform(RoundedCorners(45))
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.imagenotfound)
            .into(holder.image);
        holder.name.text = movie.name
//        holder.overview.text = movie.overview

        holder.itemView.setOnClickListener{

                val intent = Intent(it.context, TVPage::class.java).apply {
                    putExtra("NAME", movie.name)
                    putExtra("IMAGE", movie.image)
                    putExtra("OVERVIEW", movie.overview)
                    putExtra("DATE", movie.date)
                    putExtra("POPULARITY", movie.popularity.toString())
                    putExtra("VOTE_AVERAGE", movie.vote_average.toString())
                }
                startActivity(
                    it.context,
                    intent,
                    null
                )
        }
    }

    override fun getItemCount(): Int {
        return movies.size;
    }
}