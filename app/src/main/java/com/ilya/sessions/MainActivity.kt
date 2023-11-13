package com.ilya.sessions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ilya.sessions.screen.SessionsScreen
import com.ilya.theme.LocalColorScheme
import com.ilya.theme.SessionsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SessionsAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarMessage = stringResource(id = R.string.unsuccessful_add)
                val coroutineScope = rememberCoroutineScope()
                
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = LocalColorScheme.current.primary
                ) {
                    SessionsScreen(modifier = Modifier.padding(it), onSessionClick = {
                    
                    }, onUnsuccessfulAdd = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = snackbarMessage)
                        }
                    })
                }
            }
        }
    }
    
}

