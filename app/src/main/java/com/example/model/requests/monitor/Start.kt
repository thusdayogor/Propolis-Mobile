package com.example.model.requests.monitor

import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.MONITOR_START
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class Start(_discussionId:String) : Request
{
    private val discussionId = _discussionId


    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{
            append(DISCUSSION_ID_BODY, discussionId)
            append(METHOD_BODY, MONITOR_START)
        })
        return body
    }

}