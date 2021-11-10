package com.ramady.mynews.RoomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ramady.mynews.models.NewsHeadLines.Article
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface NewsDao {

    @Insert
    fun doInsert(newsList: Article):Completable

    @Query("select * from news_table")
    fun getNewsRoom(): Single<List<Article>>

    @Query("DELETE FROM news_table WHERE title = :title")
    fun delete(title : String):Completable

    @Query("SELECT EXISTS(SELECT * FROM news_table WHERE  title= :title)")
    fun isRowIsExist(title : String) : Observable<Boolean>



}