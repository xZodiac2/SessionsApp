package com.ilya.data.retrofit

import com.squareup.moshi.Json

data class Session(
    @Json(name = "date") val date: String,
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "speaker") val speaker: String,
    @Json(name = "description") val description: String,
    @Json(name = "timeInterval") val timeInterval: String,
    @Json(name = "id") val id: String,
    @Json(name = "isFavourite") val isFavourite: Boolean,
)