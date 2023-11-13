package com.ilya.data

import com.ilya.data.retrofit.Session
import com.ilya.data.retrofit.SessionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionsRepository @Inject internal constructor(
    private val api: SessionsApi,
) {
    
    suspend fun getAllSessions(): List<Session> {
        val sessions = withContext(Dispatchers.IO) { api.getAllSessions() }
        return sessions.sortedBy {
            it.date.split("\\s".toRegex())[0].toInt()
        }
    }
    
    suspend fun searchSessions(query: String): List<Session> {
        return withContext(Dispatchers.IO) {
            api.getAllSessions().filter { it.isMatchingWithQuery(query) }
        }
    }
}