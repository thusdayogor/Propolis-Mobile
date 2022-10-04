package com.example.model.requests.discussions

import com.example.model.data.DataConst.DISCUSSION_ID_BODY
import com.example.model.data.DataConst.GET_LIST_FOR_DISCUSSION
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request


class GetDiscussionsLists(_discussionId:String) : Request
{

    private val discussionId = _discussionId

    override fun makeParams(): List<Params> {
        val list = mutableListOf<Params>()
        list.add(Params(DISCUSSION_ID_BODY, discussionId))
        list.add(Params(METHOD_BODY, GET_LIST_FOR_DISCUSSION))
        return list
    }
}