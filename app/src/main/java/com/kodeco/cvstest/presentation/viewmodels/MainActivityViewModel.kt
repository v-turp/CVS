package com.kodeco.cvstest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.kodeco.cvstest.data.FlickrResponse
import com.kodeco.cvstest.networking.ApiServiceHelperImpl
import com.kodeco.cvstest.networking.RetrofitNetwork
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel: ViewModel() {

    // --- flickr response
    private val _flickrResponse = MutableStateFlow(FlickrResponse())
    var flickrResponse : StateFlow<FlickrResponse> = _flickrResponse.asStateFlow()

    // --- searchbar query
    private val _searchingSearchBarQuery = MutableStateFlow(false)
    val searchingSearchBarQuery: StateFlow<Boolean> = _searchingSearchBarQuery.asStateFlow()

    fun updateNetworkSearchingStatus(searching: Boolean){
        _searchingSearchBarQuery.value = searching
    }

    fun getFlickerDataFlow(searchQuery: String) = flickrRetrofitCallback(searchQuery)

    private fun flickrRetrofitCallback(searchQuery: String) = callbackFlow {

        val stringFilter = searchQuery.split(",").map { it.trim() }.toString()
        val retrofitCallback = object : Callback<FlickrResponse> {
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                updateNetworkSearchingStatus(searching = false)
                trySend(response.body())
                if (response.isSuccessful) {
                    _flickrResponse.value = response.body()!!
                } else {
                    // Handle unsuccessful response
                }
            }
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                updateNetworkSearchingStatus(searching = false)
                // Handle network errors
            }
        }
        ApiServiceHelperImpl(RetrofitNetwork.getGcloudApiService())
            .getFlickrData(stringFilter)
            .collect{it.enqueue(retrofitCallback)}
        awaitClose { }
    }
}