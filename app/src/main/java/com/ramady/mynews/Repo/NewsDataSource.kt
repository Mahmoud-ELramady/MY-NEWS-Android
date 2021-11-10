package com.ramady.mynews.Repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ramady.mynews.api.ApiInterface
import com.ramady.mynews.models.NewsHeadLines.Article
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsDataSource(val apiService: ApiInterface, val compositeDisposable: CompositeDisposable) {

      var _downloadNewsResponse= MutableLiveData<List<Article>>()

//    val downloadNewsResponse: LiveData<List<Article>>
//        get() = _downloadNewsResponse


    fun fetchNews(country:String,category:String){


            compositeDisposable.add(apiService.getNewsHeadLines(country,category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _downloadNewsResponse.value=it.articles

                }, {
                    Log.e("no",it.message.toString())
//                    val news= listOf(Article(402,"","","","", Source(500,""),"","","",false))
//                    _downloadNewsResponse.value=news
//
                     }


                )
            )

    }


}