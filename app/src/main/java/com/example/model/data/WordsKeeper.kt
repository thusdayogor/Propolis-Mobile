package com.example.model.data

class WordsKeeper()
{
    private lateinit var words:List<String>

    fun init(_words:List<String>)
    {
        words = _words
    }


    fun getWords():List<String>
    {
        return words
    }


    fun getWord(index:Int):String?
    {
        if(words.isEmpty()) {
            return null
        }
        return words[index]
    }
}