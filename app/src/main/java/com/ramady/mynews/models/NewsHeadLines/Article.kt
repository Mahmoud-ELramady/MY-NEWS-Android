package com.ramady.mynews.models.NewsHeadLines

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")

data class Article(


    @PrimaryKey(autoGenerate = true)
    var id: Int,

    var author: String?="",
    var content: String?="",
    var description: String?="",
    val publishedAt: String?="",
    val source: Source?=null,
    val title: String?="",
    val url: String?="",
    var urlToImage: String?="",
    var favourite:Boolean=false
)