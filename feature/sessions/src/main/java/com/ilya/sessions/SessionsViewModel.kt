package com.ilya.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.SessionsStateEvent
import com.ilya.core.TextReference
import com.ilya.data.SessionsRepository
import com.ilya.data.retrofit.Session
import com.ilya.sessions.models.GroupedSessions
import com.ilya.sessions.screen.SessionsScreenEvent
import com.ilya.sessions.screen.SessionsScreenState
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
    
    private val _screenStateFlow = MutableStateFlow<SessionsScreenState>(SessionsScreenState.Loading)
    val screenStateFlow = _screenStateFlow.asStateFlow()
    
    private val _snackbarEventStateFlow = MutableStateFlow<SessionsStateEvent>(SessionsStateEvent.Consumed)
    val snackbarEventStateFlow = _snackbarEventStateFlow.asStateFlow()
    
    private val _searchFieldValueStateFlow = MutableStateFlow("")
    val searchValueStateFlow = _searchFieldValueStateFlow.asStateFlow()
    
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _screenStateFlow.value = SessionsScreenState.Error(SessionsError.NoInternet)
    }
    
    private val sessionsList = mutableListOf<Session>()
    
    fun getFavouritesList(): List<Session> {
        return sessionsList.filter { it.isFavourite }
    }
    
    fun handleEvent(event: SessionsScreenEvent) {
        when (event) {
            is SessionsScreenEvent.Start -> onStart()
            is SessionsScreenEvent.Retry -> onRetry()
            is SessionsScreenEvent.AddFavourite -> onAddFavourite(event.session)
            is SessionsScreenEvent.Search -> onSearch(_searchFieldValueStateFlow.value)
            is SessionsScreenEvent.Input -> onInput(event.value)
        }
    }
    
    fun onSnackbarConsumed() {
        _snackbarEventStateFlow.value = SessionsStateEvent.Consumed
    }
    
    private fun onStart() {
        if (_screenStateFlow.value == SessionsScreenState.Loading) {
            getAllSessions()
        }
    }
    
    private fun onRetry() {
        getAllSessions()
    }
    
    private fun onAddFavourite(session: Session) {
        if (isFavouritesFilled() && !session.isFavourite) {
            val text = TextReference.StringRef(R.string.unsuccessful_add, FAVOURITES_LIMIT.toString())
            _snackbarEventStateFlow.value = SessionsStateEvent.Triggered(text)
            return
        }
        
        when (val stateValue = _screenStateFlow.value) {
            is SessionsScreenState.ShowSessions -> {
                val sessionIndex = getFavouriteSessionIndex(session)
                sessionsList[sessionIndex] = session.copy(isFavourite = !session.isFavourite)
                _screenStateFlow.value = SessionsScreenState.ShowSessions(grouped(sessionsList))
            }
            
            is SessionsScreenState.ShowSearchedSessions -> {
                val newState = stateValue.sessions.map { groupedSessions ->
                    val sessions = groupedSessions.sessions.map {
                        if (it == session) {
                            it.copy(isFavourite = !session.isFavourite)
                        } else {
                            it
                        }
                    }
                    GroupedSessions(groupedSessions.date, sessions)
                }
                sessionsList[getFavouriteSessionIndex(session)] = session.copy(isFavourite = !session.isFavourite)
                _screenStateFlow.value = SessionsScreenState.ShowSearchedSessions(newState)
            }
            
            else -> Unit
        }
    }
    
    private fun getFavouriteSessionIndex(session: Session, list: List<Session> = sessionsList): Int {
        return if (session.isFavourite) {
            list.indexOf(session.copy(isFavourite = true))
        } else {
            list.indexOf(session)
        }
    }
    
    private fun onInput(value: String) {
        _searchFieldValueStateFlow.value = value
    }
    
    private fun onSearch(searchBy: String) {
        _screenStateFlow.value = SessionsScreenState.Loading
        
        viewModelScope.launch {
            val sessions = repository
                .searchSessions(searchBy.trim())
                .map { session ->
                    val sessionAsFavourite = session.copy(isFavourite = true)
                    
                    if (getFavouritesList().contains(sessionAsFavourite)) {
                        sessionAsFavourite
                    } else {
                        session
                    }
                }
            
            _screenStateFlow.value =
                if (searchBy.isBlank() || searchBy.isBlank() && _screenStateFlow.value is SessionsScreenState.ShowSearchedSessions) {
                    SessionsScreenState.ShowSessions(grouped(sessionsList))
                } else {
                    SessionsScreenState.ShowSearchedSessions(grouped(sessions))
                }
        }
    }
    
    private fun getAllSessions() {
        _screenStateFlow.value = SessionsScreenState.Loading
        
        viewModelScope.launch(exceptionHandler) {
            val sessions = repository.getAllSessions()
            sessionsList.clear()
            sessionsList.addAll(sessions)
            _screenStateFlow.value = SessionsScreenState.ShowSessions(grouped(sessionsList))
        }
    }
    
    
    private fun isFavouritesFilled(): Boolean {
        return getFavouritesList().size >= 3
    }
    
    private fun grouped(sessions: List<Session>): List<GroupedSessions> {
        val grouped = mutableMapOf<String, List<Session>>()
        
        sessions.forEach {
            val group = grouped[it.date]?.toMutableList() ?: mutableListOf()
            group += it
            grouped[it.date] = group
        }
        
        return grouped.map { (key, value) ->
            GroupedSessions(key, value)
        }
    }
    
    companion object {
        private const val FAVOURITES_LIMIT = 3
    }
    
}