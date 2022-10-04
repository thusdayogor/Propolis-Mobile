package com.example.model.requests.discussions

import com.example.model.data.DataConst.DELETE_LIST_FROM_DISCUSSION
import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class DeleteListFromDiscussions(_discussionId:String,_listId:String) : Request
{
    private val discussionId = _discussionId
    private val listId = _listId

    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{

            append(DISCUSSION_ID_BODY, discussionId)
            append(LIST_ID_BODY, listId)
            append(METHOD_BODY, DELETE_LIST_FROM_DISCUSSION)
        })
        return body
    }
}