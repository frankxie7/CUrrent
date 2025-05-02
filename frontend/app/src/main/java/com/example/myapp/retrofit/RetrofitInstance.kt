package com.cornellappdev.introandroid.a6.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.getValue

@Singleton
class RetrofitInstance @Inject constructor(){
    private val okHttpClient = OkHttpClient.Builder()
        .build()

    val apiService: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
