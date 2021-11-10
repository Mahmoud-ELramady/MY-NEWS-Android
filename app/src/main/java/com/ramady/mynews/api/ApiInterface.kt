package com.ramady.mynews.api

import com.ramady.mynews.models.NewsHeadLines.NewsHeadLine
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query


//https://newsapi.org/v2/top-headlines?country=eg&category=&apiKey=da29f2094590433f9c081707357f56ce

//https://newsapi.org/v2/everything?q=%D8%A7%D9%84%D8%B3%D9%8A%D8%B3%D9%89&apiKey=da29f2094590433f9c081707357f56ce
interface ApiInterface {

    @GET("v2/top-headlines")
    fun getNewsHeadLines(@Query("country") country:String,
                         @Query("category")category:String ):Single<NewsHeadLine>


    @GET("v2/everything")
   suspend fun getSearchList(@Query("q") searchName:String): NewsHeadLine




}