package com.example.model.requests.auth

import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.REFRESH
import com.example.model.data.DataConst.REFRESH_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class Refresh(_refreshToken:String?): Request
{

    private val refreshToken = _refreshToken

    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{

            if(refreshToken!=null) {
                append(REFRESH_BODY, refreshToken)
            }
            append(METHOD_BODY,REFRESH)
            println("$REFRESH_BODY:$refreshToken")
            println("$METHOD_BODY:$REFRESH")
        })
        return body
    }
}