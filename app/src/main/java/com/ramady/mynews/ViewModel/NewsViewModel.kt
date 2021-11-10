package com.ramady.mynews.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramady.mynews.Repo.NewsRepositary
import com.ramady.mynews.models.NewsHeadLines.Article
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NewsViewModel(private val repo: NewsRepositary, country: String, category: String): ViewModel() {

    val composite= CompositeDisposable()

  val newsList:MutableLiveData<List<Article>> = repo.fetchNews(composite,country,category)


    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }


}