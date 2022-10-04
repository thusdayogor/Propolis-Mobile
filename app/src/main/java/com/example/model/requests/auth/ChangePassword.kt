package com.example.model.requests.auth

import com.example.model.data.DataConst.CHANGE_PASSWORD
import com.example.model.data.DataConst.EMAIL_BODY
import com.example.model.data.DataConst.FORBIDDEN_SYMBOLS
import com.example.model.data.DataConst.MAX_LEN
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.MIN_LEN
import com.example.model.data.DataConst.MUST_MATCH
import com.example.model.data.DataConst.NEW_PASSWORD_BODY
import com.example.model.data.DataConst.NOT_FILLED
import com.example.model.data.DataConst.NOT_MUST_MATCH
import com.example.model.data.DataConst.PASSWORD_BODY
import com.example.model.data.DataConst.SQL_INJECTION
import com.example.model.data.DataConst.TOO_LONG
import com.example.model.data.DataConst.TOO_SHORT
import com.example.model.data.DataConst.VALID
import com.example.model.requests.Request
import io.ktor.client.request.forms.*

class ChangePassword(_email:String,_oldPassword:String,_newPassword:String,_repNewPassword:String):
    Request
{

    private val email = _email
    private val oldPassword = _oldPassword
    private val newPassword = _newPassword
    private val repNewPassword = _repNewPassword


    override fun checkData(): String {

        when {
            oldPassword == newPassword -> {
                return NOT_MUST_MATCH
            }
            newPassword != repNewPassword -> {
                return MUST_MATCH
            }
            newPassword.isEmpty() || oldPassword.isEmpty() || repNewPassword.isEmpty() -> {
                return NOT_FILLED
            }
            newPassword.count() < MIN_LEN || oldPassword.count() < MIN_LEN || repNewPassword.count() < MIN_LEN -> {
                return TOO_SHORT
            }
            newPassword.count() > MAX_LEN || oldPassword.count() > MAX_LEN || repNewPassword.count() > MAX_LEN ->
            {
                return TOO_LONG
            }
            newPassword.contains(SQL_INJECTION) || oldPassword.contains(SQL_INJECTION) || repNewPassword.contains(
                SQL_INJECTION) ->
            {
                return FORBIDDEN_SYMBOLS
            }
        }
        return VALID
    }

    override fun makeRequest(): MultiPartFormDataContent
    {
        val body = MultiPartFormDataContent(formData{
            append(EMAIL_BODY,email)
            append(PASSWORD_BODY,oldPassword)
            append(NEW_PASSWORD_BODY, newPassword)
            append(METHOD_BODY, CHANGE_PASSWORD)
        })
        return body
    }
}