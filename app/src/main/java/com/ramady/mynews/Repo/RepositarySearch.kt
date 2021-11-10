package com.ramady.mynews.Repo

import com.ramady.mynews.api.ApiClient
import com.ramady.mynews.models.NewsHeadLines.NewsHeadLine

class RepositarySearch {

    suspend fun getSearchList(searchName:String): NewsHeadLine {
              return ApiClient.getClient().getSearchList(searchName)
         }

}