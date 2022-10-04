package com.example.model.requests.monitor

import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.GET_NEW_POSTS
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request

class GetNewPosts(_discussionId:String):Request
{

    private val discussionId = _discussionId

    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(METHOD_BODY, GET_NEW_POSTS))
        list.add(Params(DISCUSSION_ID_BODY, discussionId))
        return list
    }
}