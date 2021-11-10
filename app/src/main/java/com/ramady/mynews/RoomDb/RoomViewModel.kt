package com.ramady.mynews.RoomDb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramady.mynews.models.NewsHeadLines.Article
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RoomViewModel(var db: DataBase):ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    val newsListDb= MutableLiveData<List<Article>>()

    val checkTitleInDb=MutableLiveData<Boolean>()

    fun insertNews(n: Article){

        db.newsDao.doInsert(n)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneInsert","done Insert")
                }

                override fun onError(e: Throwable) {
                    Log.e("failInsert",e.toString())
                }


            })

    }


    fun getNews(){
        db.newsDao.getNewsRoom()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                newsListDb.postValue(it)
                Log.e("getNews","getNews")
            },{

            }
            ).let {
                compositeDisposable.add(it)
            }
    }


    fun deleteNews(title : String){

        db.newsDao.delete(title)
            .subscribeOn(Schedulers.computation())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Log.e("doneDelete","done Delete")
                }

                override fun onError(e: Throwable) {
                    Log.e("failDelete",e.toString())
                }


            })

    }




    fun isRowTitleExist(title:String){
        db.newsDao.isRowIsExist(title)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    checkTitleInDb.postValue(it)
                    Log.e("done",it.toString())
                },{

                }
            ).let {
                compositeDisposable.add(it)
            }

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()

    }

}