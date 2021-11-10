package com.ramady.mynews.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramady.mynews.Repo.RepositarySearch
import com.ramady.mynews.models.NewsHeadLines.NewsHeadLine
import kotlinx.coroutines.*

class ViewModelSearch(val repo: RepositarySearch):ViewModel(){

    val searchList=MutableLiveData<NewsHeadLine>()


    fun getListSearch(searchName:String){


        viewModelScope.launch {

            try {
                val response=repo.getSearchList(searchName)

                    searchList.value=response
            }catch (e:Exception){
                Log.e("newResearch",e.message.toString())
            }

        }

    }

}