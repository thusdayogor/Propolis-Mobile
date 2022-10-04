package com.example.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.model.response.SocketResponse
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.news.NewsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [msg_window.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class msg_window : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var notNewsImageView:ImageView


    private lateinit var liveDataDiscussions: MutableLiveData<List<String>>
    private lateinit var liveDataPosts:MutableLiveData<SocketResponse>
    private lateinit var liveDataOldPosts:MutableLiveData<List<SocketResponse>>
    private lateinit var liveDataCounter:MutableLiveData<Int>

    private var oldPostsResponse:MutableList<SocketResponse> = mutableListOf(SocketResponse())
    private var discussions: MutableList<String> = mutableListOf(String())
    private lateinit var postsResponse:SocketResponse
    private var counter:Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onStart() {
        super.onStart()
        notNewsImageView = activity?.findViewById(R.id.notNewsImage)!!
        initRecyclerView()
        spinnerSet()
    }


    override fun onResume() {
        super.onResume()
        println("fragment resume")
    }



    private fun spinnerSet()
    {
        val spinner = activity?.findViewById<Spinner>(R.id.msg_discussion_spinner)
        val adapter = ArrayAdapter(requireContext(), R.layout.yellow_spinner, discussions)
        spinner?.background?.setColorFilter(
                resources.getColor(R.color.main),
                PorterDuff.Mode.SRC_ATOP
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        spinner?.prompt = "Выберите обсуждение"
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                oldPostsResponse.clear()

                initRecyclerView()

                notNewsImageView?.visibility = View.VISIBLE

                activity?.let {
                    (it as MainActivity).setCurMonitorDiscussion(position)
                }

                println("Cur monitor id:$position")
            }
        }
    }


    private fun addToRecycler()
    {

        //newsAdapter.addNews(postsResponse)

    }


    private fun initRecyclerView()
    {
        newsAdapter = activity?.applicationContext?.let { NewsAdapter(it) }!!
        println("init Recycler View")
        val recyclerView = activity?.findViewById<RecyclerView>(R.id.newsList)
        with(recyclerView)
        {
            this?.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            this?.layoutManager = layoutManager
            this?.adapter = newsAdapter
            this?.addOnScrollListener( object : RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = recyclerView.layoutManager?.itemCount
                    var lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    lastVisibleItemPosition+=1
                    if (totalItemCount == lastVisibleItemPosition) {
                        Log.d("Load $lastVisibleItemPosition", "Load new list $totalItemCount")
                        println("Counter: $counter")
                        if (counter != 0) {
                            activity?.let {
                                (it as MainActivity).loadNewPosts(counter)
                            }
                        }
                    }

                    val swipeRefreshLayout = activity?.findViewById<SwipeRefreshLayout>(R.id.swipeControl)

                    swipeRefreshLayout?.setOnRefreshListener {


                        oldPostsResponse.clear()

                        initRecyclerView()


                        activity?.let {
                            (it as MainActivity).startRefreshPosts()
                        }

                        println("I want to refresh")

                        swipeRefreshLayout.isRefreshing = false
                    }

                }
            })
        }
    }


    fun setArgs(_liveDataDiscussions:MutableLiveData<List<String>>,_liveDataPosts:MutableLiveData<SocketResponse>,_liveDateOldPosts:MutableLiveData<List<SocketResponse>>
    ,_liveDataCounter:MutableLiveData<Int>)
    {
        liveDataDiscussions = _liveDataDiscussions
        liveDataPosts = _liveDataPosts
        liveDataOldPosts = _liveDateOldPosts
        liveDataCounter = _liveDataCounter

        liveDataDiscussions.observe(this, Observer {

            discussions.clear()

            liveDataDiscussions.value?.let { it1 -> discussions.addAll(it1) }

            spinnerSet()

            println("hello from discussions: $discussions")
        })

        liveDataPosts.observe(this, Observer {

            postsResponse = liveDataPosts.value!!

            println("hello from new posts $postsResponse")

            //addToRecycler()
        })


        liveDataOldPosts.observe(this, Observer {


            oldPostsResponse.clear()

            liveDataOldPosts.value?.let { it1 -> oldPostsResponse.addAll(it1) }

            oldPostsResponse.reverse()

            notNewsImageView?.visibility = View.GONE

            oldPostsResponse.forEach {
                newsAdapter.addNews(it)
            }

            counter -= oldPostsResponse.size

            println("hello from old posts $oldPostsResponse")

        })


        liveDataCounter.observe(this, Observer {

            counter = liveDataCounter.value!!

            println("hello from counter $counter")

        })

    }

    override fun onPause() {
        println("pause fragment")
        activity?.let {
            (it as MainActivity).clearMonitorDiscussion(null)
        }
        super.onPause()
    }



    override fun onDestroy() {

        println("destroy fragment")
        super.onDestroy()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.msg_window, container, false)
    }

    companion object {

        fun newInstance() = msg_window()
    }
}