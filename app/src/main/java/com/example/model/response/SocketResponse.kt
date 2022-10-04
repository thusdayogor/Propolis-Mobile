package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SocketResponse (
    val discussion_id:Int? = null,
    val text:String? = null,
    val link:String? = null,
    val message_num:Int? = null,
    val date:Int? = null,
    val user_link:String? = null,
    var attachment_links:String? = null,
)

