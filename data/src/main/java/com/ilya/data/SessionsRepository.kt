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
    
    suspend fun getSessionById(id: Int): Session {
        return withContext(Dispatchers.IO) { api.getAllSessions() }.first { it.id.toInt() == id }
    }
    
}