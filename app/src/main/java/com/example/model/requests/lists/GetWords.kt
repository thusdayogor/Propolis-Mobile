package com.example.model.requests.lists


import com.example.model.data.DataConst.GET_WORDS
import com.example.model.data.DataConst.LIST_ID_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.Params
import com.example.model.requests.Request



class GetWords(_listId:String) : Request
{

    private val listId = _listId


    override fun makeParams(): List<Params>
    {
        val list = mutableListOf<Params>()
        list.add(Params(LIST_ID_BODY,listId))
        list.add(Params(METHOD_BODY, GET_WORDS))
        return list
    }

}