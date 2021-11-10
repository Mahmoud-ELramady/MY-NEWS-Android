package com.ramady.mynews.RoomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ramady.mynews.models.NewsHeadLines.Article

@Database(entities = [Article::class],version = 1)
@TypeConverters(Converters::class)

abstract class DataBase: RoomDatabase() {

    abstract val newsDao : NewsDao

    companion object{
        @Volatile
        private var INSTANCE: DataBase?= null

        fun getInstance(context: Context): DataBase {
            synchronized(this){
                var instance: DataBase?= INSTANCE
                if (instance==null){
                    instance= Room.databaseBuilder(context.applicationContext
                        , DataBase::class.java
                        ,"news_data_base")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

    }

}