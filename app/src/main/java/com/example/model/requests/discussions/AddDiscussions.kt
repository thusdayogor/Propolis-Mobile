package com.example.model.requests.discussions

import android.util.Log
import com.example.model.data.DataConst.ADD_DISCUSSIONS
import com.example.model.data.DataConst.DESCRIPTION_BODY
import com.example.model.data.DataConst.DESCRIPTION_LONG
import com.example.model.data.DataConst.DESCRIPTION_SHORT
import com.example.model.data.DataConst.DIS_MAX_LEN
import com.example.model.data.DataConst.FORBIDDEN_SYMBOLS
import com.example.model.data.DataConst.GROUP_ID_BODY
import com.example.model.data.DataConst.LINK_LONG
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.MIN_LEN
import com.example.model.data.DataConst.NOT_DIS_LINK
import com.example.model.data.DataConst.NOT_FILLED
import com.example.model.data.DataConst.SQL_INJECTION
import com.example.model.data.DataConst.SQL_LINK_INJECTION
import com.example.model.data.DataConst.TOPIC_ID_BODY
import com.example.model.data.DataConst.VALID
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class AddDiscussions(_link:String,_description:String) : Request
{

    private val link = _link
    private val description = _description

    lateinit var groupId:String
    lateinit var topicId:String

    companion object
    {
        const val LINK_ID_SEPARATOR = "-"
        const val GROUP_TOPIC_SEPARATOR = "_"
    }


    override fun checkData(): String
    {
         when {
            description.isEmpty() -> {
                return NOT_FILLED
            }
            description.length > DIS_MAX_LEN -> {
                return DESCRIPTION_LONG
            }
            description.length < MIN_LEN -> {
                return DESCRIPTION_SHORT
            }
            description.contains(SQL_INJECTION) -> {
                return FORBIDDEN_SYMBOLS
            }
        }


        when {
            link.isEmpty() -> {
                return NOT_FILLED
            }
            link.length > DIS_MAX_LEN -> {
                return LINK_LONG
            }
            link.contains(SQL_LINK_INJECTION) -> {
                return FORBIDDEN_SYMBOLS
            }
        }

        //example https://vk.com/topic-2038990_48337061

        val idLink = link.substringAfter(LINK_ID_SEPARATOR,"")

        Log.d("IdLink",idLink)

        if (idLink.isNullOrEmpty())
        {
            return NOT_DIS_LINK
        }

        groupId = idLink.substringBeforeLast(GROUP_TOPIC_SEPARATOR,"")
        topicId = idLink.substringAfterLast(GROUP_TOPIC_SEPARATOR,"")

        Log.d("groupId",idLink)
        Log.d("topicId",idLink)

        if(groupId.isNullOrEmpty() || topicId.isNullOrEmpty())
        {
            return NOT_DIS_LINK
        }

        return VALID
    }

    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{

            append(GROUP_ID_BODY,groupId)
            append(TOPIC_ID_BODY,topicId)
            append(DESCRIPTION_BODY,description)
            append(METHOD_BODY, ADD_DISCUSSIONS)
        })
        return body

    }
}