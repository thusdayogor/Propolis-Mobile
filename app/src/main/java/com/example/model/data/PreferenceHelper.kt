package com.example.model.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.data.DataConst.TABLE_NAME

class PreferenceHelper(context: Context) {

    private val pref:SharedPreferences = context.getSharedPreferences(TABLE_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()


    fun saveDate(row:String, value:String?)
    {
        editor.putString(row,value)
        editor.apply()
    }

    fun checkData(): Boolean {
        return pref.getString(DataConst.EMAIL_ROW, null).isNullOrEmpty()
    }

    fun getData(row:String):String?
    {
        return pref.getString(row,null)
    }

}