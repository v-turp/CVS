package com.kodeco.cvstest.networking

import com.kodeco.cvstest.data.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1}")
    fun getFlickrData(@Query("tags") tags: String): Call<FlickrResponse>
}