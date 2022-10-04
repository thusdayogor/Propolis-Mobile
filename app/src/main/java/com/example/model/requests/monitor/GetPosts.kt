package com.example.model.requests.monitor

import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.GET_POSTS
import com.example.model.data.DataConst.MESSAGE_NUM_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request

class GetPosts(_discussionId:String,_messageNum:String):Request {

    private val discussionId = _discussionId
    private val messageNum = _messageNum

    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(METHOD_BODY, GET_POSTS))
        list.add(Params(DISCUSSION_ID_BODY, discussionId))
        list.add(Params(MESSAGE_NUM_BODY, messageNum))
        return list
    }
}