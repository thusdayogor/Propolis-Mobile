package com.example.model.data

import com.example.model.response.Discussion

class DiscussionKeeper {
    private var discussion: List<Discussion> = listOf()

    fun init(_discussion: List<Discussion>) {
        discussion = _discussion
    }


    fun getDiscussion(): List<Discussion> {
        return discussion
    }

    fun getDiscussionId(index:Int):Int?
    {
        if (discussion.isNotEmpty())
        {
            return discussion[index].id
        }

        return null
    }

}