package com.ramady.mynews.Repo

import androidx.lifecycle.MutableLiveData
import com.ramady.mynews.api.ApiInterface
import com.ramady.mynews.models.NewsHeadLines.Article
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NewsRepositary(private val api: ApiInterface) {
    lateinit var newsDataSource: NewsDataSource

    fun fetchNews(compositeDisposable: CompositeDisposable,country:String,category:String): MutableLiveData<List<Article>> {
       newsDataSource= NewsDataSource(api,compositeDisposable)
       newsDataSource.fetchNews(country,category)
        return newsDataSource._downloadNewsResponse

    }




}