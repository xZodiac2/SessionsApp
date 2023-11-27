package com.ilya.sessions.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ilya.sessions.R
import com.ilya.sessions.SessionsViewModel
import com.ilya.sessions.screen.SessionsScreenEvent
import com.ilya.theme.LocalColorScheme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
fun LazyListScope.search(viewModel: SessionsViewModel) {
    item {
        val searchValue by viewModel.searchValueStateFlow.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                value = searchValue,
                onValueChange = { viewModel.handleEvent(SessionsScreenEvent.Input(it)) },
                leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.handleEvent(SessionsScreenEvent.Search)
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.handleEvent(SessionsScreenEvent.Search)
                    keyboardController?.hide()
                }),
                placeholder = { Text(text = stringResource(id = R.string.search)) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = LocalColorScheme.current.primaryTextColor,
                    containerColor = LocalColorScheme.current.containerTextFieldColor,
                    focusedLeadingIconColor = LocalColorScheme.current.leadingIconTextFieldColor,
                    focusedIndicatorColor = LocalColorScheme.current.indicatorTextFieldColor,
                    focusedTrailingIconColor = LocalColorScheme.current.trailingIconTextFieldColor,
                    cursorColor = LocalColorScheme.current.primaryTextColor,
                    unfocusedIndicatorColor = LocalColorScheme.current.indicatorTextFieldColor,
                    unfocusedLeadingIconColor = LocalColorScheme.current.leadingIconTextFieldColor,
                    unfocusedTrailingIconColor = LocalColorScheme.current.trailingIconTextFieldColor,
                    placeholderColor = LocalColorScheme.current.placeholderTextFieldColor
                )
            )
        }
    }
}