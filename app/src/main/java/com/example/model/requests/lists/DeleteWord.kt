package com.example.model.requests.lists

import com.example.model.data.DataConst.DELETE_WORD
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.WORDS_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class DeleteWord(_word:String,_listId:String) :Request
{
    private var word = _word
    private val listId = _listId


    override fun makeRequest(): MultiPartFormDataContent
    {

        val body = MultiPartFormDataContent(formData{

            append(WORDS_BODY,word)
            append(LIST_ID_BODY,listId)
            append(METHOD_BODY, DELETE_WORD)
        })
        return body
    }
}