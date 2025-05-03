package com.example.myapp.retrofit

import retrofit2.Response
import retrofit2.http.GET

// matches your backend's listing fields
data class Listing(
    val id: Int,
    val vehicle_type: String,
    val price_per_day: Double,
    val available_from: String,
    val available_to: String,
    val is_rented: Boolean
)

data class ListingResponse(
    val listings: List<Listing>
)

interface ApiService {
    @GET("api/listings/")
    suspend fun getListings(): Response<ListingResponse>
}
