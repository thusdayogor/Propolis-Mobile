package com.example.model.сlient

import android.util.Log
import com.example.model.data.Params
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.response.*
import io.ktor.http.*
import io.ktor.util.cio.*


class Client {


    companion object ClientConst
    {
        const val HOST = "propolis.space"
        const val POST = "POST"
        //const val PORT = 4005
    }

    private lateinit var client:HttpClient


    private suspend fun getRequest(params: List<Params>, path: String): HttpResponse {

        println("Params:" +params.toString())

        val response: HttpResponse = client.get(path) {

            params.forEach{
                parameter(it.key,it.value)
            }
        }
        return response
    }

    private suspend fun postRequest(request: MultiPartFormDataContent, path:String): HttpResponse
    {
        val response: HttpResponse = client.post(path) {
            body = request
        }
        return response
    }


    suspend fun sendHttp(
        request: MultiPartFormDataContent, params: List<Params>,
        path: String, accessToken: String?, method: String
    ):String {


        client = HttpClient(Android){

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                    //port = PORT
                }

                if(accessToken!=null)
                {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
            }
        }



        var response:HttpResponse =
            if (method == POST)
            postRequest(request = request, path = path)
        else
            getRequest(params = params,path = path)


        val answer = String(response.content.toByteArray())

        Log.d("response", response.status.toString())

        client.close()

        return answer
    }

}