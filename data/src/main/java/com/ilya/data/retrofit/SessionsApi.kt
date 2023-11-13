package com.ilya.data.retrofit

import retrofit2.http.GET


internal interface SessionsApi {
    
    @GET("/AJIEKCX/901e7ae9593e4afd136abe10ca7d510f/raw/61e7c1f037345370cf28b5ae6fdaffdd9e7e18d5/Sessions.json")
    suspend fun getAllSessions(): List<Session>
    
    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com"
    }
    
}