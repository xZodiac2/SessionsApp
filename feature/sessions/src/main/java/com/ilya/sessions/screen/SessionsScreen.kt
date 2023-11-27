package com.ilya.sessions.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilya.EventEffect
import com.ilya.sessions.R
import com.ilya.sessions.SessionsViewModel
import com.ilya.sessions.screen.favourites.favourites
import com.ilya.sessions.screen.search.search
import com.ilya.sessions.screen.sessions.sessions
import com.ilya.theme.LocalColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onSessionClick: (Int) -> Unit,
    sessionsViewModel: SessionsViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarEvent by sessionsViewModel.snackbarEventStateFlow.collectAsState()
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalColorScheme.current.primary
    ) { paddingValues ->
        Content(
            sessionsViewModel = sessionsViewModel,
            onSessionClick = onSessionClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
    
    EventEffect(
        event = snackbarEvent,
        onConsumed = sessionsViewModel::onSnackbarConsumed,
        action = { snackbarHostState.showSnackbar(it) }
    )
    
    LaunchedEffect(key1 = Unit) {
        sessionsViewModel.handleEvent(SessionsScreenEvent.Start)
    }
}

@Composable
private fun Content(
    sessionsViewModel: SessionsViewModel,
    onSessionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sessionsState by sessionsViewModel.screenStateFlow.collectAsState()
    
    LazyColumn(
        modifier = modifier
            //.padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        search(sessionsViewModel)
        item {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.favorites_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColorScheme.current.primaryTextColor
            )
        }
        favourites(sessionsViewModel.getFavouritesList(), onSessionClick = onSessionClick)
        item {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = stringResource(id = R.string.sessions_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColorScheme.current.primaryTextColor
            )
        }
        sessions(
            sessionsState = sessionsState,
            onFavouriteClick = {
                sessionsViewModel.handleEvent(SessionsScreenEvent.AddFavourite(it))
            },
            onSessionClick = onSessionClick,
            onTryAgainClick = {
                sessionsViewModel.handleEvent(SessionsScreenEvent.Retry)
            }
        )
    }
}