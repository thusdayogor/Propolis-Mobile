package com.example.model.data

import com.example.model.response.ListForDiscussions

class DiscussionListsKeeper {

    private lateinit var listsForDiscussions:List<ListForDiscussions>

    fun init(_listForDiscussions:List<ListForDiscussions>)
    {
        listsForDiscussions = _listForDiscussions
    }


    fun getListsForDiscussions():List<ListForDiscussions>
    {
        return listsForDiscussions
    }

    fun getIdLists(index:Int):Int?
    {
        if(listsForDiscussions.isEmpty())
            return null

        println("Discussion name for delete:" + listsForDiscussions[index].name)

        return listsForDiscussions[index].list_id
    }

}