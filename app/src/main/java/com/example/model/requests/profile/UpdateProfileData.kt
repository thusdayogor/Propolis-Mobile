package com.example.model.requests.profile

import com.example.model.data.DataConst
import com.example.model.data.DataConst.FORBIDDEN_SYMBOLS
import com.example.model.data.DataConst.MAX_LEN
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.NAME_BODY
import com.example.model.data.DataConst.NOT_FILLED
import com.example.model.data.DataConst.SQL_INJECTION
import com.example.model.data.DataConst.SQL_LOGIN_INJECTION
import com.example.model.data.DataConst.TOO_LONG
import com.example.model.data.DataConst.UPDATE_PROFILE
import com.example.model.data.DataConst.VALID
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class UpdateProfileData(_name:String) : Request {

    private val name = _name

    override fun checkData(): String
    {
        return when {
            name.isEmpty() -> {
                NOT_FILLED
            }
            name.length > MAX_LEN -> {
                TOO_LONG
            }
            name.contains(SQL_LOGIN_INJECTION) -> {
                FORBIDDEN_SYMBOLS
            }
            else -> VALID
        }

    }


    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{

            append(NAME_BODY,name)
            //append(METHOD_BODY, UPDATE_PROFILE)
        })
        return body
    }
}