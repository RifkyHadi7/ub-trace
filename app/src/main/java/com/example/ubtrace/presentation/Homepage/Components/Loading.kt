package com.example.ubtrace.presentation.Homepage.Components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loading(isLoading: Boolean) {
    if (isLoading){
        Column (modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(
                modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally)

            )
        }

    }
}