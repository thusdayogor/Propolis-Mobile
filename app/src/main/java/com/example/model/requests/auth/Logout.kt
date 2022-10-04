package com.example.model.requests.auth

import com.example.model.data.DataConst.REFRESH_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class Logout(_refreshToken:String?): Request
{

    private val refreshToken = _refreshToken

    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{
            if(refreshToken!=null) {
                append(REFRESH_BODY, refreshToken)
            }
        })
        return body
    }
}