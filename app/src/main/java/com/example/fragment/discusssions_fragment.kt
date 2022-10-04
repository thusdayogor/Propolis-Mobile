package com.example.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.MainActivity
import com.example.myapplication.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [discusssions_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class discusssions_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var liveDateDiscussions:MutableLiveData<List<String>>
    private lateinit var liveDateBlack:MutableLiveData<List<String>>
    private lateinit var liveDateWhite:MutableLiveData<List<String>>
    private lateinit var liveDateListsForDiscussions:MutableLiveData<List<String>>

    private var discussions: MutableList<String> = mutableListOf(String())
    private var whitelist: MutableList<String> = mutableListOf(String())
    private var blacklist: MutableList<String> = mutableListOf(String())
    private var listsForDiscussions: MutableList<String> = mutableListOf(String())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart()
    {
        super.onStart()
        setSpinnerDiscussions()
        setSpinnerWhite()
        setSpinnerBlack()
        setSpinnerListsForDiscussion()
    }


    private fun setSpinnerDiscussions()
    {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerDiscussions)
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

                activity?.let {
                        (it as MainActivity).setCurDiscussionId(position)
                }

                println("Cur description choose:$position")
            }
        }
    }



    private fun setSpinnerWhite()
    {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerDiscussionsWhiteLists)
        val adapter = ArrayAdapter(requireContext(), R.layout.yellow_spinner, whitelist)
        spinner?.background?.setColorFilter(
            resources.getColor(R.color.main),
            PorterDuff.Mode.SRC_ATOP
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        spinner?.prompt = "Выберите белый список"
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {

                activity?.let {
                    (it as MainActivity).setCurWhiteDiscussion(position)
                }

                println("Cur white position:$position")
            }
        }
    }



    private fun setSpinnerBlack()
    {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerDiscussionsBlackLists)
        val adapter = ArrayAdapter(requireContext(), R.layout.yellow_spinner, blacklist)
        spinner?.background?.setColorFilter(
            resources.getColor(R.color.main),
            PorterDuff.Mode.SRC_ATOP
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        spinner?.prompt = "Выберите чёрный список"
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {

                activity?.let {
                    (it as MainActivity).setCurBlackDiscussion(position)
                }

                println("Cur black position:$position")
            }
        }
    }


    private fun setSpinnerListsForDiscussion()
    {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerListForDiscussions)
        val adapter = ArrayAdapter(requireContext(), R.layout.yellow_spinner, listsForDiscussions)
        spinner?.background?.setColorFilter(
            resources.getColor(R.color.main),
            PorterDuff.Mode.SRC_ATOP
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        spinner?.prompt = "Выберите список"
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {

                activity?.let {
                    (it as MainActivity).setCurListsForDiscussion(position)
                }
                println("Cur list for discussion position:$position")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discusssions_fragment, container, false)
    }




    fun setDiscussionList(_liveDateDiscussions:MutableLiveData<List<String>>,_liveDateWhite:MutableLiveData<List<String>>,
                          _liveDateBlack:MutableLiveData<List<String>>,_liveDateListsForDiscussions:MutableLiveData<List<String>>)
    {
        liveDateDiscussions = _liveDateDiscussions
        liveDateWhite = _liveDateWhite
        liveDateBlack = _liveDateBlack
        liveDateListsForDiscussions = _liveDateListsForDiscussions

        liveDateDiscussions.observe(this, Observer {

            discussions.clear()
            liveDateDiscussions.value?.let { it1 -> discussions.addAll(it1) }

            setSpinnerDiscussions()

            if(discussions.isEmpty())
            {
                listsForDiscussions = mutableListOf()
                setSpinnerListsForDiscussion()
            }

            println("hello from discussions: $discussions")
        })

        liveDateWhite.observe(this, Observer {

            whitelist.clear()
            liveDateWhite.value?.let { it1 -> whitelist.addAll(it1) }

            setSpinnerWhite()

            println("hello from white: $whitelist")
        })

        liveDateBlack.observe(this, Observer {

            blacklist.clear()
            liveDateBlack.value?.let { it1 -> blacklist.addAll(it1) }

            setSpinnerBlack()

            println("hello from black: $blacklist")
        })

        liveDateListsForDiscussions.observe(this, Observer {

            listsForDiscussions.clear()
            liveDateListsForDiscussions.value?.let { it1 -> listsForDiscussions.addAll(it1) }

            setSpinnerListsForDiscussion()

            println("hello from lists for discussions $listsForDiscussions")
        })
    }

    companion object {

        fun newInstance() =
            discusssions_fragment()

    }
}