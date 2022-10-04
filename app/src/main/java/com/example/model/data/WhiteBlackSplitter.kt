package com.example.model.data

import com.example.model.response.Catalog

class WhiteBlackSplitter
{

    private lateinit var allLists:List<Catalog>
    private val blackLists:MutableList<Catalog> = mutableListOf()
    private val whiteLists:MutableList<Catalog> = mutableListOf()


    fun init(_allLists:List<Catalog>)
    {
        allLists = _allLists
        blackLists.clear()
        whiteLists.clear()
    }


    fun splitAll()
    {
        allLists.forEach {

            if (it.white == true)
            {
                whiteLists.add(it)
            }
            else if (it.white == false)
            {
                blackLists.add(it)
            }
        }
    }

    fun getWhiteLists():List<Catalog>
    {
        return whiteLists
    }


    fun getBlackLists():List<Catalog>
    {
        return blackLists
    }


    fun getNamesLists(catalogList:List<Catalog>):List<String>
    {
        var list = mutableListOf<String>()

        catalogList.forEach {
            list.add(it.name.toString())
        }

        return list
    }



}