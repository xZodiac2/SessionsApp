package com.ilya.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.SessionsRepository
import com.ilya.data.retrofit.Session
import com.ilya.sessions.screen.sessions.SessionScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val repository: SessionsRepository,
) : ViewModel() {
    
    private val _sessionsStateFlow = MutableStateFlow<SessionsState>(SessionsState.Loading)
    val sessionsStateFlow = _sessionsStateFlow.asStateFlow()
    
    private val _favouritesStateFlow = MutableStateFlow<MutableList<Session>>(mutableListOf())
    val favouritesStateFlow = _favouritesStateFlow.asStateFlow()
    
    var favouritesAddStatus = true
        private set
    
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _sessionsStateFlow.value = SessionsState.Error(SessionsError.NoInternet)
    }
    
    fun handleEvent(event: SessionScreenEvent) {
        when (event) {
            is SessionScreenEvent.Start -> onStart()
            is SessionScreenEvent.Retry -> onRetry()
            is SessionScreenEvent.Search -> onSearch(event.query)
            is SessionScreenEvent.AddFavourite -> onAddFavourite(event.session)
        }
    }
    
    private fun onAddFavourite(session: Session) {
        val favourites = _favouritesStateFlow.value
        
        if (favourites.size >= 3 && session.isFavourite) {
            favouritesAddStatus = false
            return
        }
        
        val sessionsState = _sessionsStateFlow.value as SessionsState.ShowSessions
        val sessionIndex = sessionsState.sessions.indexOf(session.copy(isFavourite = !session.isFavourite))
        _sessionsStateFlow.value = SessionsState.ShowSessions(sessionsState.sessions.also {
            it[sessionIndex] = session
        })
        
        if (session.isFavourite) {
            favourites += session
        } else {
            favourites -= session.copy(isFavourite = true)
        }
        
        favouritesAddStatus = true
    }
    
    private fun onSearch(query: String) {
        searchSessions(query)
    }
    
    private fun onStart() {
        if (_sessionsStateFlow.value == SessionsState.Loading) {
            getAllSessions()
        }
    }
    
    private fun onRetry() {
        getAllSessions()
    }
    
    private fun getAllSessions() {
        _sessionsStateFlow.value = SessionsState.Loading
        
        viewModelScope.launch(exceptionHandler) {
            val sessions = repository.getAllSessions()
            _sessionsStateFlow.value = SessionsState.ShowSessions(sessions.toMutableList())
        }
    }
    
    private fun searchSessions(query: String) {
        _sessionsStateFlow.value = SessionsState.Loading
        
        viewModelScope.launch(exceptionHandler) {
            val sessions = repository.searchSessions(query)
            _sessionsStateFlow.value = SessionsState.ShowSessions(sessions.toMutableList())
        }
    }
    
}