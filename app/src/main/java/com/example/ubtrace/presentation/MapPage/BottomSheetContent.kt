package com.example.ubtrace.presentation.MapPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ubtrace.data.report.Report

@Composable
fun BottomSheetContent(report: Report) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = report.image,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(text = "Item: ${report.item}")
        Text(text = "Description: ${report.description}")
    }
}