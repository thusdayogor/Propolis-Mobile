package com.example.fragment

import android.graphics.Color.parseColor
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fragment.filter.FilterConst.ADD_LIST_TEXT_BLACK
import com.example.fragment.filter.FilterConst.ADD_LIST_TEXT_WHITE
import com.example.fragment.filter.FilterConst.TEXTVIEW_BLACK
import com.example.fragment.filter.FilterConst.ADD_WORDS_TEXT_BUTTON_BLACK
import com.example.fragment.filter.FilterConst.ADD_WORDS_TEXT_BUTTON_WHITE
import com.example.fragment.filter.FilterConst.BLACK_COLOR
import com.example.fragment.filter.FilterConst.DELETE_LIST_TEXT_BLACK
import com.example.fragment.filter.FilterConst.DELETE_LIST_TEXT_WHITE
import com.example.fragment.filter.FilterConst.DELETE_WORD_TEXT_BLACK
import com.example.fragment.filter.FilterConst.DELETE_WORD_TEXT_WHITE
import com.example.fragment.filter.FilterConst.GREY_HINT_COLOR
import com.example.fragment.filter.FilterConst.TEXTVIEW_WHITE
import com.example.fragment.filter.FilterConst.YELLOW_COLOR
import com.example.fragment.filter.FilterConst.YELLOW_HINT_COLOR
import com.example.myapplication.MainActivity
import com.example.myapplication.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [filter.newInstance] factory method to
 * create an instance of this fragment.
 */
class filter : Fragment(), CompoundButton.OnCheckedChangeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var liveDateWhite:LiveData<List<String>>
    private lateinit var liveDateBlack:LiveData<List<String>>
    private lateinit var liveDateWords:LiveData<List<String>>



    private var blackList: MutableList<String> = mutableListOf(String())
    private var whiteList: MutableList<String> = mutableListOf(String())
    private var wordsList: MutableList<String> = mutableListOf(String())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.findViewById<Switch>(R.id.switchList)?.setOnCheckedChangeListener(this)
        setSpinnerListsSettings(blackList, R.color.main,R.layout.yellow_spinner)
        setSpinnerWordsSettings(wordsList,R.color.main,R.layout.yellow_spinner)
    }



    private fun setSpinnerWordsSettings(date:List<String>, color:Int, layout:Int)
    {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerWord)
        val adapter = ArrayAdapter(requireContext(), layout, date)
        spinner?.background?.setColorFilter(
            resources.getColor(color),
            PorterDuff.Mode.SRC_ATOP
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        spinner?.prompt = "Выберите слово"
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {

                activity?.let {
                        (it as MainActivity).setCurWordId(position)
                }

                println("Cur word:$position")
            }
        }
    }


    private fun setSpinnerListsSettings(date:List<String>, color:Int, layout:Int) {
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerList)
        val adapter = ArrayAdapter(requireContext(), layout, date)
        spinner?.background?.setColorFilter(
            resources.getColor(color),
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

                val switch = activity?.findViewById<Switch>(R.id.switchList)
                val white = switch?.isChecked

                activity?.let {
                    if (white != null) {
                        (it as MainActivity).setCurListId(white,position)
                    }
                }

                println("Cur position:$position")
                println("Cur white:$white")
            }
        }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    companion object {

        fun newInstance(): filter {
            return filter()
        }
    }



    fun setArgs(_liveDateWhite:MutableLiveData<List<String>>, _liveDateBlack:MutableLiveData<List<String>>,
                _liveDateWords:MutableLiveData<List<String>>)
    {
        liveDateWhite = _liveDateWhite
        liveDateBlack = _liveDateBlack
        liveDateWords = _liveDateWords

        liveDateBlack.observe(this, Observer {

            blackList.clear()

            liveDateBlack.value?.let { it1 -> blackList.addAll(it1) }

            val checked = activity?.findViewById<Switch>(R.id.switchList)?.isChecked

            if(checked==false)
            {
                setSpinnerListsSettings(blackList, R.color.main, R.layout.yellow_spinner)
            }

            if(blackList.isEmpty() && checked==false) {
                wordsList = mutableListOf()
                setSpinnerWordsSettings(wordsList,R.color.black,R.layout.black_spinner)
            }

            println("hello from black: $blackList")
        })

        liveDateWhite.observe(this,Observer{

            whiteList.clear()

            liveDateWhite.value?.let { it1 -> whiteList.addAll(it1) }


            val checked = activity?.findViewById<Switch>(R.id.switchList)?.isChecked

            if(checked==true)
            {
                setSpinnerListsSettings(whiteList, R.color.black, R.layout.black_spinner)
            }

            if(whiteList.isEmpty() && checked==true) {
                wordsList = mutableListOf()
                setSpinnerWordsSettings(wordsList, R.color.main, R.layout.yellow_spinner)
            }


            println("hello from white: $whiteList")
        })

        liveDateWords.observe(this, Observer {

            wordsList.clear()

            liveDateWords.value?.let { it1 -> wordsList.addAll(it1) }

            val checked = activity?.findViewById<Switch>(R.id.switchList)?.isChecked

            if(checked==true) {
                setSpinnerWordsSettings(wordsList,R.color.black,R.layout.black_spinner)
            }
            else
            {
                setSpinnerWordsSettings(wordsList, R.color.main, R.layout.yellow_spinner)
            }

            if(blackList.isEmpty() && checked==false) {
                wordsList = mutableListOf()
            }

            if(whiteList.isEmpty() && checked==true) {
                wordsList = mutableListOf()
            }

            println("hello from words: $wordsList")
        })
    }



    object FilterConst
    {
        //textView
        const val TEXTVIEW_WHITE = "Белый список"
        const val TEXTVIEW_BLACK = "Чёрный список"
        //add button
        const val ADD_WORDS_TEXT_BUTTON_WHITE = "Добавить в белый список"
        const val ADD_WORDS_TEXT_BUTTON_BLACK = "Добавить в чёрный список"
        //add list button
        const val ADD_LIST_TEXT_BLACK = "Создать чёрный список"
        const val ADD_LIST_TEXT_WHITE = "Создать белый список"
        //delete list button
        const val DELETE_LIST_TEXT_BLACK = "Удалить чёрный список"
        const val DELETE_LIST_TEXT_WHITE = "Удалить белый список"
        //delete word button
        const val DELETE_WORD_TEXT_BLACK = "Удалить из чёрного списка"
        const val DELETE_WORD_TEXT_WHITE = "Удалить из белого списка"


        //color
        val YELLOW_COLOR = parseColor("#FDE910")
        val BLACK_COLOR = parseColor("#FF000000")
        val YELLOW_HINT_COLOR = parseColor("#FFFF99")
        val GREY_HINT_COLOR = parseColor("#808080")
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean)
    {
        val constraintLayout = activity?.findViewById<ConstraintLayout>(R.id.ConstraintFilter)
        val textView = activity?.findViewById<TextView>(R.id.textViewStateList)
        val editAddWord = activity?.findViewById<EditText>(R.id.addWordsEditText)
        val editAddList = activity?.findViewById<EditText>(R.id.addListEditText)
        val buttonAddWords = activity?.findViewById<Button>(R.id.addWordListButton)
        val buttonAddList = activity?.findViewById<Button>(R.id.addListButton)
        val buttonDelList = activity?.findViewById<Button>(R.id.deleteListButton)
        val buttonDelWord = activity?.findViewById<Button>(R.id.deleteWordButton)


        //buttonSet
        buttonStyleChange(isChecked,buttonAddWords)
        buttonStyleChange(isChecked,buttonAddList)
        buttonStyleChange(isChecked,buttonView)
        buttonStyleChange(isChecked,buttonDelList)
        buttonStyleChange(isChecked,buttonDelWord)


        //textViewSet
        textViewStyleChange(isChecked,textView)

        //editTextSet
        editTextStyleChange(isChecked,editAddList)
        editTextStyleChange(isChecked,editAddWord)

        //constraintSet
        layoutStyleChange(isChecked,constraintLayout)


        if(isChecked)
        {
            textView?.text = TEXTVIEW_WHITE
            buttonAddWords?.text = ADD_WORDS_TEXT_BUTTON_WHITE
            buttonAddList?.text = ADD_LIST_TEXT_WHITE
            buttonDelList?.text = DELETE_LIST_TEXT_WHITE
            buttonDelWord?.text = DELETE_WORD_TEXT_WHITE
            setSpinnerListsSettings(whiteList, R.color.black, R.layout.black_spinner)
            if(whiteList.isEmpty()) {
                wordsList = mutableListOf()
            }
            setSpinnerWordsSettings(wordsList, R.color.black, R.layout.black_spinner)
        }
        else
        {
            textView?.text = TEXTVIEW_BLACK
            buttonAddWords?.text = ADD_WORDS_TEXT_BUTTON_BLACK
            buttonAddList?.text = ADD_LIST_TEXT_BLACK
            buttonDelList?.text = DELETE_LIST_TEXT_BLACK
            buttonDelWord?.text = DELETE_WORD_TEXT_BLACK
            setSpinnerListsSettings(blackList, R.color.main,R.layout.yellow_spinner)
            if(blackList.isEmpty()) {
                wordsList = mutableListOf()
            }
            setSpinnerWordsSettings(wordsList,R.color.main,R.layout.yellow_spinner)
        }
    }


    private fun layoutStyleChange(white:Boolean, constraintLayout: ConstraintLayout?)
    {

        val resource = if (white) {
            R.color.main
        } else {
            R.color.black
        }

        constraintLayout?.setBackgroundResource(resource)
    }

    private fun editTextStyleChange(white: Boolean,editText: EditText?)
    {
        val hintColor:Int
        val textColor:Int
        val backgroundStyle:Int

        if(white)
        {
            textColor = BLACK_COLOR
            hintColor = GREY_HINT_COLOR
            backgroundStyle = R.drawable.white_edit_style
        }
        else
        {
            textColor = YELLOW_COLOR
            hintColor = YELLOW_HINT_COLOR
            backgroundStyle = R.drawable.edit_text_style
        }
        editText?.setBackgroundResource(backgroundStyle)
        editText?.setHintTextColor(hintColor)
        editText?.setTextColor(textColor)

    }


    private fun textViewStyleChange(white:Boolean, textView: TextView?)
    {

        var textColor = if (white) {
            BLACK_COLOR
        } else {
            YELLOW_COLOR
        }

        textView?.setTextColor(textColor)
    }


    private fun buttonStyleChange(white:Boolean,button:Button?)
    {

        var textColor:Int
        var backgroundStyle:Int

        if (white)
        {
            textColor = YELLOW_COLOR
            backgroundStyle = R.drawable.black_button_style
        }
        else
        {
            textColor = BLACK_COLOR
            backgroundStyle = R.drawable.button_style
        }

        button?.setBackgroundResource(backgroundStyle)
        button?.setTextColor(textColor)
    }
}

