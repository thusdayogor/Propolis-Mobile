package com.example.model.requests.auth

import com.example.model.data.DataConst.EMAIL_BODY
import com.example.model.data.DataConst.FORBIDDEN_SYMBOLS
import com.example.model.data.DataConst.MAX_LEN
import com.example.model.data.DataConst.METHOD_BODY
import com.example.model.data.DataConst.MIN_LEN
import com.example.model.data.DataConst.MUST_MATCH
import com.example.model.data.DataConst.NOT_FILLED
import com.example.model.data.DataConst.PASSWORD_BODY
import com.example.model.data.DataConst.REGISTER
import com.example.model.data.DataConst.SQL_INJECTION
import com.example.model.data.DataConst.SQL_LOGIN_INJECTION
import com.example.model.data.DataConst.TOO_LONG
import com.example.model.data.DataConst.TOO_SHORT
import com.example.model.data.DataConst.VALID
import com.example.model.requests.Request
import io.ktor.client.request.forms.*


class Registration(_email:String, _password:String,  _passwordRep:String): Request
{
    private val email = _email
    private val password = _password
    private val passwordRep = _passwordRep


    override fun checkData(): String
    {
        when {
            email.isEmpty() || password.isEmpty()  || passwordRep.isEmpty()->
            {
                return NOT_FILLED
            }
            password!=passwordRep->
            {
                return MUST_MATCH
            }
            email.count() > MAX_LEN  || password.count() > MAX_LEN ->
            {
                return TOO_LONG
            }
            email.count() < MIN_LEN  || password.count() < MIN_LEN ->
            {
                return TOO_SHORT
            }
            email.contains(SQL_LOGIN_INJECTION) || password.contains(SQL_INJECTION) || passwordRep.contains(
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
           append(EMAIL_BODY, email)
           append(PASSWORD_BODY, password)
           append(METHOD_BODY, REGISTER)
       })
       return body
    }

}