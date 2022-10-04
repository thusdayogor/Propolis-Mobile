package com.example.model.requests.profile

import com.example.model.data.DataConst
import com.example.model.data.DataConst.ADD_VK
import com.example.model.data.DataConst.LOGIN_BODY
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.PASSWORD_BODY
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class AddVk(_login:String, _password:String) : Request
{
    private val login = _login
    private val password = _password


    override fun checkData(): String
    {
        when {
            login.isEmpty() || password.isEmpty() -> {
                return DataConst.NOT_FILLED
            }
            login.count() < DataConst.MIN_LEN || password.count() < DataConst.MIN_LEN -> {
                return DataConst.TOO_SHORT
            }
            login.count() > DataConst.MAX_LEN -> {
                return DataConst.TOO_LONG
            }
        }
        return DataConst.VALID

    }


    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData {

            append(LOGIN_BODY,login)
            append(PASSWORD_BODY,password)
            append(METHOD_BODY, ADD_VK)
        })
        return body
    }
}