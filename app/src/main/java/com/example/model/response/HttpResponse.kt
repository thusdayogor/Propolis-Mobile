package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse (
    val counter:Int? = null,
    val result:Boolean? = null,
    val method:String? = null,
    val errorMessage:String? = null,
    val error: String? = null,
    val accessToken:String? = null,
    val refreshToken:String? = null,
    val email:String? = null,
    val profile:Profile? = null,
    val list:List<Catalog>? = null,
    val discussions:List<Discussion>? = null,
    val discussion_lists:List<ListForDiscussions>? = null,
    val posts:List<SocketResponse>?=null
)


@Serializable
data class Discussion(
    val id:Int? = null,
    val groupId:Int? = null,
    val topicId:Int? = null,
    val description:String? = null,
    val user:String? = null,
    val isStarted:Boolean? = null
)

@Serializable
data class ListForDiscussions(
    val list_id:Int? = null,
    val name:String? = null,
    val white:Boolean? = null
)

@Serializable
data class Catalog(
    val id:Int? = null,
    val name:String? = null,
    val white:Boolean? = null,
    val word:String? = null,
)


@Serializable
data class Profile(
    val name:String? = null,
    val confirmed:Boolean? = null,
    val email:String? = null
)







