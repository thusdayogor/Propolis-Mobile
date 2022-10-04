package com.example.model.requests.lists

import com.example.model.data.DataConst.GET_LISTS
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request

class GetLists: Request {

    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(METHOD_BODY, GET_LISTS))
        return list
    }
}