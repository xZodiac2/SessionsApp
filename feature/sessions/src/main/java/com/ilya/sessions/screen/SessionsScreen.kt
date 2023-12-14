package com.ilya.sessions.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.ilya.sessions.screen.favourites.Favourites
import com.ilya.sessions.screen.search.SearchContent
import com.ilya.sessions.screen.sessions.Sessions
import com.ilya.theme.LocalColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    onSessionClick: (String) -> Unit,
    quit: () -> Unit,
    sessionsViewModel: SessionsViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarEvent by sessionsViewModel.snackbarEventStateFlow.collectAsState()
    val alertDialogState by sessionsViewModel.alertDialogStateFlow.collectAsState()
    
    BackHandler {
        sessionsViewModel.handleEvent(SessionsScreenEvent.BackPress)
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = LocalColorScheme.current.primary
    ) { paddingValues ->
        Content(
            sessionsViewModel = sessionsViewModel,
            onSessionClick = onSessionClick,
            modifier = Modifier.padding(paddingValues)
        )
        if (alertDialogState is AlertDialogState.Triggered) {
            QuitAlertDialog(
                sessionsViewModel,
                alertDialogState as AlertDialogState.Triggered,
                quit
            )
        }
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
    onSessionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sessionsState by sessionsViewModel.screenStateFlow.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SearchContent(
            viewModel = sessionsViewModel
        )
        FavouritesContent(
            sessionsViewModel = sessionsViewModel,
            onSessionClick = onSessionClick
        )
        SessionsContent(
            sessionsViewModel = sessionsViewModel,
            sessionsState = sessionsState,
            onSessionClick = onSessionClick
        )
    }
}

@Composable
private fun FavouritesContent(sessionsViewModel: SessionsViewModel, onSessionClick: (String) -> Unit) {
    if (sessionsViewModel.getFavouritesList().isNotEmpty()) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = stringResource(id = R.string.favorites_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColorScheme.current.primaryTextColor
        )
        Favourites(
            favouriteList = sessionsViewModel.getFavouritesList(),
            onSessionClick = onSessionClick
        )
    }
}

@Composable
private fun SessionsContent(
    sessionsViewModel: SessionsViewModel,
    sessionsState: SessionsScreenState,
    onSessionClick: (String) -> Unit,
) {
    Text(
        modifier = Modifier.padding(start = 20.dp),
        text = stringResource(id = R.string.sessions_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = LocalColorScheme.current.primaryTextColor
    )
    
    Sessions(
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

@Composable
private fun QuitAlertDialog(
    sessionsViewModel: SessionsViewModel,
    state: AlertDialogState.Triggered,
    quit: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = state.title.resolve())
        },
        confirmButton = {
            Button(onClick = {
                quit()
                sessionsViewModel.onAlertDialogConsumed()
            }) {
                Text(text = state.confirmButtonText.resolve())
            }
        },
        dismissButton = {
            Button(onClick = { sessionsViewModel.onAlertDialogConsumed() }) {
                Text(text = state.dismissButtonText.resolve())
            }
        },
        onDismissRequest = {
            sessionsViewModel.onAlertDialogConsumed()
        },
        containerColor = LocalColorScheme.current.cardContainerColor,
        titleContentColor = LocalColorScheme.current.primaryTextColor
    )
}