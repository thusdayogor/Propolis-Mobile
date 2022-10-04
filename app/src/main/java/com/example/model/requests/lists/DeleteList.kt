package com.example.model.requests.lists

import com.example.model.data.DataConst.DELETE_LIST
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class DeleteList(_listId:String):Request {

    private val listId = _listId

    override fun makeRequest(): MultiPartFormDataContent {

        val body = MultiPartFormDataContent(formData{

            append(LIST_ID_BODY,listId)
            append(METHOD_BODY, DELETE_LIST)
        })
        return body
    }
}