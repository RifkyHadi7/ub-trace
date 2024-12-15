package com.example.ubtrace.presentation.Homepage.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppbarHome(
    title: String,
    onLogoutClick: () -> Unit
){
    TopAppBar(
        title = {
            Text(text = title,
                modifier = Modifier.fillMaxWidth().padding(start = 0.dp, end = 14.dp),
                textAlign = TextAlign.Center)
        },
        actions = {
            IconButton(onClick = { onLogoutClick() }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout"
                )
            }
        }
    )
}

@Composable
@Preview
fun preview(){
    topAppbarHome(
        title = "HALAMAN",
        onLogoutClick = TODO()
    )
}