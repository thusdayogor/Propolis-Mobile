package com.example.model.requests.profile

import com.example.model.data.DataConst.GET_PROFILE
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request

class GetProfile : Request {


    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(METHOD_BODY, GET_PROFILE))
        return list
    }
}