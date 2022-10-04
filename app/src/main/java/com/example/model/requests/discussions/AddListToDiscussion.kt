package com.example.model.requests.discussions

import com.example.model.data.DataConst.ADD_DISCUSSIONS
import com.example.model.data.DataConst.ADD_LIST_TO_DISCUSSION
import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.TYPE_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class AddListToDiscussion(_listId:String,_discussionId:String,_white:String) : Request
{
    private val listId = _listId
    private val discussionId = _discussionId
    private val white = _white


    override fun makeRequest(): MultiPartFormDataContent {


        val body = MultiPartFormDataContent(formData{

            append(LIST_ID_BODY, listId)
            append(DISCUSSION_ID_BODY, discussionId)
            append(TYPE_BODY, white)
            append(METHOD_BODY, ADD_LIST_TO_DISCUSSION)
        })
        return body


    }
}