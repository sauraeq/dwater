package com.waterdelivery.Checkout.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R


class MoviesAdapter(private val moviesList: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var year: TextView
        var genre: TextView

        init {
            title = view.findViewById<View>(R.id.prod_name) as TextView
            genre = view.findViewById<View>(R.id.qty) as TextView
            year = view.findViewById<View>(R.id.price) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_checkout1, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.title.text = movie.title
        holder.genre.text = movie.genre

        val calc:Float= movie.year!!.toFloat()* movie.genre!!.toFloat()
        holder.year.text = "AED "+calc.toString()
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}