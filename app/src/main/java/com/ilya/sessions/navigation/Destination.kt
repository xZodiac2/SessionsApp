package com.ilya.sessions.navigation

sealed class Destination(val route: String) {
    object MainScreen : Destination("main")
    object SessionDetailsScreen : Destination("sessionDetails")
    
    fun withArgumentNames(vararg names: String): String {
        return route + names.joinToString(prefix = "/{", separator = "}/{", postfix = "}")
    }
    
    fun withArguments(vararg args: String): String {
        return route + args.joinToString(prefix = "/", separator = "/")
    }
    
}