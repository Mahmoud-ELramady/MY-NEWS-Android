package com.ramady.mynews.RoomDb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ramady.mynews.models.NewsHeadLines.Source

class Converters {
    @TypeConverter
    fun fromSourceToString(s: Source):String{
        return Gson().toJson(s)
    }

    @TypeConverter
    fun fromStringToSource(value:String?): Source {
        val s=object : TypeToken<Source>(){}.type
        return Gson().fromJson(value,s)
    }
}