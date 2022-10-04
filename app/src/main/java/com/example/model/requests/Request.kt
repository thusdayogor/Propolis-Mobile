package com.example.model.requests

import com.example.model.data.DataConst
import com.example.model.data.DataConst.SQL_INJECTION
import com.example.model.data.DataConst.VALID
import com.example.model.data.Params
import io.ktor.client.request.forms.*



interface Request
{

    fun makeParams(): List<Params> {

        return mutableListOf()
    }

    fun makeRequest():MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{

        })
        return body
    }

    fun checkData():String
    {
        return VALID
    }

}
