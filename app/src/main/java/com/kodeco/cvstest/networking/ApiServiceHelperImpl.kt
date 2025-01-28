package com.kodeco.cvstest.networking

import kotlinx.coroutines.flow.flow

class ApiServiceHelperImpl(private val apiService: ApiService) : ApiServiceHelper {

    override fun getFlickrData(searchTag: String) = flow{
        emit(apiService.getFlickrData(searchTag))
    }
}