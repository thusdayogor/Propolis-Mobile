package com.example.model.push

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.model.data.DataConst.ACCESS_TOKEN_ROW
import com.example.model.data.DataConst.NOTIFICATION_ROW
import com.example.model.data.DataConst.ON
import com.example.model.data.PreferenceHelper
import com.example.model.requests.discussions.GetDiscussions
import com.example.model.requests.monitor.GetNewPosts
import com.example.model.response.HttpResponse
import com.example.model.сlient.Client
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

class PushService: Service() {


    private lateinit var client: Client
    private lateinit var noticeHelper:NoticeHelper

    companion object {
        const val GET_NEW_POSTS_PATH = "/monitor/get_new_posts"
        const val DISCUSSIONS_GET_PATH = "/discussions/get/"
    }

    override fun onBind(p0: Intent?): IBinder? = null;


    override fun onCreate() {
        super.onCreate()
        Log.d("PushService: ", "Service create");
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("PushService: ","Service start")
        noticeHelper = NoticeHelper(applicationContext)

        val prefHelper = PreferenceHelper(applicationContext)

        val alarmHelper = AlarmHelper(applicationContext)


        client = Client()

        if (prefHelper.getData(NOTIFICATION_ROW)==ON)
        {
            val token = prefHelper.getData(ACCESS_TOKEN_ROW)
            token?.let { discussions(it) }
        }

        alarmHelper.create()

        //noticeHelper.create("Я запустился", "Но нет ничего нового")

        return START_STICKY
    }


    private fun discussions(token:String)
    {
        val getDiscussions = GetDiscussions()

        val params = getDiscussions.makeParams()

        val request = getDiscussions.makeRequest()

        val path = DISCUSSIONS_GET_PATH

        val method = "GET"

        CoroutineScope(Dispatchers.IO).launch{
            val message: Deferred<String> = async {
                client.sendHttp(request,params,path,token,method)
            }

            val answer = message.await()

            val response = convertResponseToJson(answer)

            val discussions = response.discussions

            Log.d("Launch service discussion ", answer)

            discussions?.forEach {

                println("Discussions: ${it.isStarted}")
                if(it.isStarted==true)
                {
                    println("Discussions send: ${it.id}")

                    var delay:Long = (100 *it.id!!).toLong()
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            newPosts(token,it.id.toString())
                        }
                    }, delay)
                }
            }
        }

    }


    private fun newPosts(token:String, discussionId:String)
    {
        val getNewPosts = GetNewPosts(discussionId)

        val params = getNewPosts.makeParams()

        val request = getNewPosts.makeRequest()

        val path = GET_NEW_POSTS_PATH

        val method = "GET"


        CoroutineScope(Dispatchers.IO).launch{
            val message: Deferred<String> = async {
                client.sendHttp(request,params,path,token,method)
            }

            val answer = message.await()

            Log.d("Жду ответ для:", discussionId)

            Log.d("Ответ в строке для ($discussionId)", answer)

            val response = convertResponseToJson(answer)

            Log.d("Ответ в json для ($discussionId)", response.toString())

            noticeHelper = NoticeHelper(applicationContext)

            val newPosts = response.posts

            Log.d("Посты для ($discussionId):", response.posts.toString())

            newPosts?.forEach{
                noticeHelper.create("Новый комментарий", it.text.toString())
            }
        }
    }



    private fun convertResponseToJson(answer: String): HttpResponse {
        return Json { ignoreUnknownKeys = true }.decodeFromString(answer)
    }



    override fun onDestroy() {
        Log.d("AppService:","Service stop")
        super.onDestroy()
    }


}



