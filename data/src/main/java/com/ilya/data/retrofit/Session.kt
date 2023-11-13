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
) {
    fun isMatchingWithQuery(query: String): Boolean {
        val combinations = listOf(
            speaker,
            speaker.replace("\\s".toRegex(), ""),
            description,
            description.replace("\\s".toRegex(), "")
        )
        
        return combinations.any { it.contains(query, ignoreCase = true) }
    }
}