package com.cornellappdev.introandroid.a6.retrofit

import com.example.demo6_starter.util.Sentiment
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    //TODO 1: Replace 'endpoint' below with the endpoint for the API you're using
    @GET("endpoint")
    suspend fun getData(
        //TODO 2: Add queries and your api header
    ) //TODO 4: Add the return type of your function here.
}

//TODO 3: Data class that models what the API will return

//TODO (Optional): an empty version of your data class.