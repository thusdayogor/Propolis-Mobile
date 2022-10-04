package com.example.model.requests.discussions

import com.example.model.data.DataConst.GET_DISCUSSIONS
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request

class GetDiscussions : Request {

    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(METHOD_BODY,GET_DISCUSSIONS))
        return list
    }
}