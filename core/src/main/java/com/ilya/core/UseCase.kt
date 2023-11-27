package com.ilya.core

interface UseCase<in R, out T> {
    suspend operator fun invoke(data: R): Result<T>
}