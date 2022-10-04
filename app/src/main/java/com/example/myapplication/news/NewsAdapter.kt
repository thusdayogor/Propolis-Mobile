package com.example.myapplication.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.model.response.SocketResponse
import com.example.myapplication.R

class NewsAdapter(_context: Context) : RecyclerView.Adapter<NewsViewHolder>()
{

    private val newsList = mutableListOf<SocketResponse>()
    private val context = _context

    fun addNews(news:SocketResponse)
    {
        newsList.add(news)
        println("Add data: $news")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
    {
        val itemView =
           LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        return NewsViewHolder(itemView,context)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int)
    {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size

}