package com.ramady.mynews.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramady.mynews.models.NewsHeadLines.Details

class DetailsViewModel:ViewModel() {

     var _detailsData=MutableLiveData<Details>()
//    val detailsData:LiveData<Details>
//        get() =_detailsData



    fun instertDetails(details: Details){
        _detailsData.postValue(details)
        Log.e("insered","insert")
    }

    fun getDetails():MutableLiveData<Details>{
        Log.e("error","ccvcccc")

        return _detailsData
    }

}