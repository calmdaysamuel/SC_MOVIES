package com.calmday.scmovies

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TVPage() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tv_page)

        findViewById<TextView>(R.id.showName).text = intent.getStringExtra("OVERVIEW")
        findViewById<TextView>(R.id.showOverview).text = intent.getStringExtra("NAME")
        findViewById<TextView>(R.id.showRating).text = "Date: " + intent.getStringExtra("DATE")
        findViewById<TextView>(R.id.showPopularity).text ="Popularity: " + intent.getStringExtra("POPULARITY")
        findViewById<TextView>(R.id.showVote).text ="Popularity: " + intent.getStringExtra("VOTE_AVERAGE")
//        findViewById<TextView>(R.id.showPoster).text = intent.getStringExtra("IMAGE")

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/" + intent.getStringExtra("IMAGE"))
            .transform(RoundedCorners(45))
//            .placeholder(R.drawable.placeholder)
//            .error(R.drawable.imagenotfound)
            .into(findViewById<ImageView>(R.id.showPoster));
    }
}