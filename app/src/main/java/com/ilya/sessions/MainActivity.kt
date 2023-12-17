package com.ilya.sessions

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ilya.sessiondetails.screen.SessionDetailsScreen
import com.ilya.sessions.navigation.Destination
import com.ilya.sessions.screen.SessionsScreen
import com.ilya.theme.SessionsAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SessionsAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Destination.MainScreen.route) {
                    composable(Destination.MainScreen.route) {
                        SessionsScreen(
                            onSessionClick = {
                                navController.navigate(
                                    Destination.SessionDetailsScreen.withArguments(it)
                                )
                            },
                            quit = {
                                val startMain = Intent(Intent.ACTION_MAIN)
                                startMain.addCategory(Intent.CATEGORY_HOME)
                                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(startMain)
                            }
                        )
                    }
                    composable(
                        route = Destination.SessionDetailsScreen.withArgumentNames(KEY_SESSION_ID),
                        arguments = listOf(navArgument(KEY_SESSION_ID) { type = NavType.StringType })
                    ) { backStackEntry ->
                        SessionDetailsScreen(
                            sessionId = backStackEntry.arguments?.getString(KEY_SESSION_ID) ?: DEFAULT_SESSION_ID,
                            onBackClick = {
                                navController.navigate(Destination.MainScreen.route) {
                                    launchSingleTop = true
                                    popUpTo(Destination.MainScreen.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    private companion object {
        const val KEY_SESSION_ID = "sessionId"
        const val DEFAULT_SESSION_ID = ""
    }
    
}