package com.example.model.requests.lists

import com.example.model.data.DataConst.ADD_WORDS
import com.example.model.data.DataConst.FORBIDDEN_SYMBOLS
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.MAX_LEN
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.NOT_FILLED
import com.example.model.data.DataConst.SQL_WORD_INJECTION
import com.example.model.data.DataConst.VALID
import com.example.model.data.DataConst.WORDS_BODY
import com.example.model.data.DataConst.WORD_LONG
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class AddWords(_enteredWords:String,_listId:Int) : Request
{
    private val listId = _listId
    private var enteredWords =  _enteredWords
    private var sendToWords:MutableList<String> = mutableListOf()
    private var sendString  = ""

    companion object
    {
        const val SEMICOLON = ";"
    }

    override fun checkData(): String
    {
        if(enteredWords.isEmpty())
        {
            return NOT_FILLED
        }
        else if(enteredWords.contains(SQL_WORD_INJECTION))
        {
            return FORBIDDEN_SYMBOLS
        }

        enteredWords = enteredWords.replace("\n".toRegex(), "")

        enteredWords = enteredWords.replace("\r".toRegex(), "")

        sendToWords = enteredWords.split(SEMICOLON.toRegex()).toMutableList()

        sendToWords.forEach {

            when {

                it.length > MAX_LEN -> {
                    return WORD_LONG

                }
                it.isEmpty() -> {
                    sendToWords.remove(it)
                }
            }
        }

       println("Result check: " + sendToWords.toString())


        for (word in sendToWords)
        {
            val array = word.toCharArray()
            if(array[0]==' ')
            {
                var i = 0
                while(i<array.size)
                {
                    if(array[i]==' ')
                    {
                        i++
                        continue
                    }
                    break
                }

                val curWord = word.substring(i)
                println("WORD: $curWord")
                if(curWord.isNotBlank() && curWord.isNotEmpty())
                    sendString += curWord + SEMICOLON
                continue
            }
            println("WORD: $word")
            sendString += word + SEMICOLON
        }

        sendString = sendString.substringBeforeLast(SEMICOLON)

        return VALID
    }


    override fun makeRequest(): MultiPartFormDataContent
    {


        println("Send String: $sendString")


        val body = MultiPartFormDataContent(formData{

            append(WORDS_BODY,sendString)
            append(LIST_ID_BODY,listId.toString())
            append(METHOD_BODY,ADD_WORDS)
        })
        return body
    }

}