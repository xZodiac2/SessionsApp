package com.ilya

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import com.ilya.core.TextReference

@Composable
@NonRestartableComposable
fun EventEffect(event: SessionsStateEvent, onConsumed: () -> Unit, action: suspend (String) -> Unit) {
    val text = (event as? SessionsStateEvent.Triggered)?.text?.resolve()
    LaunchedEffect(key1 = event) {
        if (event is SessionsStateEvent.Triggered) {
            text?.let { action(it) }
            onConsumed()
        }
    }
}

interface SessionsStateEvent {
    data class Triggered(val text: TextReference) : SessionsStateEvent
    object Consumed : SessionsStateEvent
}
