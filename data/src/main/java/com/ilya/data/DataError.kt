package com.ilya.data

sealed class DataError(override val message: String? = null) : Throwable(message) {
    object FavouriteAlreadyExist : DataError()
    object FavouriteNotFound : DataError()
}