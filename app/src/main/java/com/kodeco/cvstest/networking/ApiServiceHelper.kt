package com.kodeco.cvstest.networking

import com.kodeco.cvstest.data.FlickrResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface ApiServiceHelper {

    fun getFlickrData(searchTag: String) : Flow<Call<FlickrResponse>>
}