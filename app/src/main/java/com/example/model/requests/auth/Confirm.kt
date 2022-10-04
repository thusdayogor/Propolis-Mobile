package com.example.model.requests

import com.example.model.data.DataConst.CODE_BODY
import com.example.model.data.DataConst.CODE_LEN
import com.example.model.data.DataConst.CONFIRM
import com.example.model.data.DataConst.EMAIL_BODY
import com.example.model.data.DataConst.INVALID_LENGTH_CODE
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.VALID
import io.ktor.client.request.forms.*

class Confirm (_email:String, _code:String) : Request
{

    private val email = _email
    private val code = _code

    override fun checkData(): String
    {
        if (code.length != CODE_LEN)
        {
            return INVALID_LENGTH_CODE
        }

        return VALID
    }


    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{
            append(EMAIL_BODY,email)
            append(CODE_BODY, code)
            append(METHOD_BODY, CONFIRM)
        })
        return body
    }
}