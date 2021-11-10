package com.ramady.mynews.api

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



//da29f2094590433f9c081707357f56ce

//93346c6aaa7f43e48b8f584a3f8937ac   //2
//30a89b39547d47f1ba89ca962206b56c   //3
//1ab1a6897b924a15a68a93ec7cfeb316   //4
const val BASE_URL="https://newsapi.org/"

object ApiClient {

    const val API_KEY_2="93346c6aaa7f43e48b8f584a3f8937ac"
    const val API_KEY_3="30a89b39547d47f1ba89ca962206b56c"
    const val API_KEY_4="1ab1a6897b924a15a68a93ec7cfeb316"
    var API_KEY="da29f2094590433f9c081707357f56ce"

    var tryCount=0

    fun getClient(): ApiInterface {

//        lateinit var response: Response
        lateinit var requestInterceptor:Interceptor
        lateinit var url: HttpUrl
        lateinit var request: Request

// to put API Key in the url
         requestInterceptor= Interceptor{ chain ->

             Log.e("main", tryCount.toString())


              url= chain.request()
                .url
                .newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build()

             request=chain.request()
                .newBuilder()
                .url(url)
                .build()

            var response=chain.proceed(request)

             while ( (!response.isSuccessful && tryCount <3) || (response.code==429 && tryCount <3) ){

                 if (tryCount ==0){
                     API_KEY = API_KEY_2
                     Log.e("count", tryCount.toString())
                     Log.e("api2", "api2")


                     url= chain.request()
                         .url
                         .newBuilder()
                         .addQueryParameter("apiKey", API_KEY)
                         .build()

                     request=chain.request()
                         .newBuilder()
                         .url(url)
                         .build()

                 }else if (tryCount ==1){
                     API_KEY = API_KEY_3
                     Log.e("count", tryCount.toString())
                     Log.e("api3", "api3")


                     url= chain.request()
                         .url
                         .newBuilder()
                         .addQueryParameter("apiKey", API_KEY)
                         .build()

                     request=chain.request()
                         .newBuilder()
                         .url(url)
                         .build()

                 }else if (tryCount ==2){
                     API_KEY = API_KEY_4
                     Log.e("count", tryCount.toString())
                     Log.e("api4", "api4")


                     url= chain.request()
                         .url
                         .newBuilder()
                         .addQueryParameter("apiKey", API_KEY)
                         .build()

                     request=chain.request()
                         .newBuilder()
                         .url(url)
                         .build()

                 }

                 response.close()
                 tryCount++

                 response=chain.proceed(request)


             }


             return@Interceptor response

         }




        val okHttpClient= OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60,TimeUnit.SECONDS)
            .build()


        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }




}