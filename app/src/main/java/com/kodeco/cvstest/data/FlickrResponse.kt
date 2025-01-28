package com.kodeco.cvstest.data

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime


data class FlickrResponse(
    val title: String? = null,
    val link: String? = null,
    val description: String? = null,
    @SerializedName("ZoneDateTime")val modified: ZonedDateTime? = null,
    val generator: String? = null,
    val items: List<Item>? = null
)

data class Item(
    val title: String,
    val link: String,
    val media: Media,
    val dateTaken: String,
    val description: String,
    val published: String,
    val author: String,
    val authorId: String,
    val tags: String
)

data class Media(
    val m: String
)