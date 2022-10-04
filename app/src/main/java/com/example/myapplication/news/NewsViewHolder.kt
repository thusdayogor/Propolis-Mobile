package com.example.myapplication.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.model.response.SocketResponse
import com.example.myapplication.R
import java.util.*


class NewsViewHolder(itemView: View, _context: Context) : RecyclerView.ViewHolder(itemView)
{


    private val appContext = _context


    fun bind(news: SocketResponse)
    {
        with(itemView)
        {
            println("Holder Bind: $news")


            val curDate: Calendar = Calendar.getInstance()
            curDate.timeInMillis = news.date!!.toLong() * 1000

            var month = curDate.get(Calendar.MONTH).toString()

            if(month.toInt()<10)
            {
                month = "0$month"
            }


            var day = curDate.get(Calendar.DAY_OF_MONTH).toString()

            if(day.toInt()<10)
            {
                day = "0$day"
            }


            val textDate =
                    day + "." +
                    month + "." +
                    curDate.get(Calendar.YEAR)
            println(textDate)


            val newsDateTextView = findViewById<TextView>(R.id.newsDateTextView)
            newsDateTextView.text = textDate


            var minute = curDate.get(Calendar.MINUTE).toString()

            if(minute.toInt() < 10)
            {
                minute = "0$minute"
            }


            var hour = curDate.get(Calendar.HOUR_OF_DAY).toString()

            if(hour.toInt() < 10)
            {
                hour = "0$hour"
            }


            val textTime = "$hour:$minute"
            val newsTimeTextView = findViewById<TextView>(R.id.newsTimeTextView)
            newsTimeTextView.text = textTime

            val newsPostTextView = findViewById<TextView>(R.id.newsTextView)
            newsPostTextView.text = news.text

            val postImageButton = findViewById<ImageButton>(R.id.newsPostLinkButton)
            postImageButton.setOnClickListener {

                val postIntent = Intent(Intent.ACTION_VIEW)
                postIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                postIntent.data = Uri.parse(news.link)
                postIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                appContext.startActivity(postIntent)
                println("Hello from post link ${news.link}")
            }


            val userImageButton = findViewById<ImageButton>(R.id.newsUserLinkButton)
            userImageButton.setOnClickListener {
                val userIntent = Intent(Intent.ACTION_VIEW)
                userIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                userIntent.data = Uri.parse(news.user_link)
                userIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                appContext.startActivity(userIntent)
                println("Hello from user link ${news.user_link}")
            }





            val imageView = findViewById<ImageView>(R.id.newsImageView)

            if(news.attachment_links!=" " || news.attachment_links != "" || !news.attachment_links.isNullOrEmpty())
            {

                println("Photo: ${news.attachment_links}")
                println("Int :${news.attachment_links?.toByteArray()}")

                Glide.with(appContext)
                    .load(news.attachment_links)
                    .into(imageView)
            }
            else
            {
                news.attachment_links=" "
                imageView.visibility = View.GONE
                println("NOT PHOTO")
            }

        }
    }
}