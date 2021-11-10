package com.ramady.mynews.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramady.mynews.Repo.RepositarySearch

class SearchViewModelProvider(val repositary: RepositarySearch) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSearch::class.java)){
            return ViewModelSearch(repositary) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")    }

}