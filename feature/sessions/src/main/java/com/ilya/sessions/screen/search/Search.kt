package com.ilya.sessions.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ilya.sessions.R
import com.ilya.theme.LocalColorScheme

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
fun SearchContent(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var inputStateValue by rememberSaveable { mutableStateOf("") }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            value = inputStateValue,
            onValueChange = { inputStateValue = it },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            trailingIcon = {
                if (inputStateValue.isNotBlank()) {
                    IconButton(
                        onClick = {
                            inputStateValue = ""
                            onSearch(inputStateValue)
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(inputStateValue)
                keyboardController?.hide()
            }),
            placeholder = { Text(text = stringResource(id = R.string.search)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = LocalColorScheme.current.primaryTextColor,
                containerColor = LocalColorScheme.current.containerTextFieldColor,
                focusedLeadingIconColor = LocalColorScheme.current.leadingIconTextFieldColor,
                focusedIndicatorColor = LocalColorScheme.current.focusedIndicatorTextFieldColor,
                disabledIndicatorColor = LocalColorScheme.current.unfocusedIndicatorTextFieldColor,
                focusedTrailingIconColor = LocalColorScheme.current.trailingIconTextFieldColor,
                cursorColor = LocalColorScheme.current.primaryTextColor,
                unfocusedIndicatorColor = LocalColorScheme.current.unfocusedIndicatorTextFieldColor,
                unfocusedLeadingIconColor = LocalColorScheme.current.leadingIconTextFieldColor,
                unfocusedTrailingIconColor = LocalColorScheme.current.trailingIconTextFieldColor,
                placeholderColor = LocalColorScheme.current.placeholderTextFieldColor,
            )
        )
    }
}
