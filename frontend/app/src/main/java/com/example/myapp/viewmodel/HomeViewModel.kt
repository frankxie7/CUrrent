package com.example.myapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.retrofit.Listing
import com.example.myapp.retrofit.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val retrofitInstance: RetrofitInstance
) : ViewModel() {

    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    val listings: StateFlow<List<Listing>> = _listings
    var selectedListing: Listing? by mutableStateOf(null)
    init {
        println("HomeViewModel initialized")  // Add this
        fetchListings()
    }


    private fun fetchListings() {
        viewModelScope.launch {
            try {
                val response = retrofitInstance.apiService.getListings()
                println("RESPONSE: $response")

                if (response.isSuccessful) {
                    val body = response.body()
                    println("BODY: $body")

                    body?.let {
                        _listings.value = it.listings
                    }
                } else {
                    println("FAILED RESPONSE: ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("EXCEPTION: ${e.localizedMessage}")
            }
        }
    }


    fun refresh() {
        fetchListings()
    }
}
