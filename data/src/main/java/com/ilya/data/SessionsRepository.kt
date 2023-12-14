package com.ilya.data

import com.ilya.data.retrofit.Session
import com.ilya.data.retrofit.SessionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsRepository @Inject internal constructor(
    private val api: SessionsApi,
) {
    
    suspend fun getAllSessions(): List<Session> {
        return withContext(Dispatchers.IO) { api.getAllSessions() }
    }
    
    suspend fun searchSessions(query: String): List<Session> {
        return withContext(Dispatchers.IO) { api.getAllSessions() }.filter { it.isMatchingWithQuery(query) }
    }
    
    suspend fun getSessionById(id: String): Session {
        return withContext(Dispatchers.IO) { api.getAllSessions() }.first { it.id == id }
    }
    
    private fun Session.isMatchingWithQuery(query: String): Boolean {
        val combinations = listOf(
            speaker,
            speaker.replace("\\s".toRegex(), ""),
            description,
            description.replace("\\s".toRegex(), "")
        )
        
        return combinations.any { it.contains(query, ignoreCase = true) }
    }
    
}