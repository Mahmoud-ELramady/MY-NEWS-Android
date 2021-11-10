package com.ramady.mynews.models.NewsHeadLines

import java.io.Serializable


data class Details (

    val   urlImage: String,
    val   name: String,
    val  date: String,
    val   title: String,
    val   desc: String,
    val content: String,
    val urlLink: String,
    var fav:Boolean

):Serializable
