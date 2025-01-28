package com.kodeco.cvstest.networking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNetwork {

    //-- HttpLoggingInterceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //-- OkHttpClient
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    //-- Gson
    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    //-- Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.flickr.com")  // TODO don't hardcode in production
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
    fun getRetrofitInstance(): Retrofit = retrofit

    fun getGcloudApiService(): ApiService = getRetrofitInstance()
        .create((ApiService::class.java))
}