package com.example.ubtrace.presentation.StatusScreen.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ubtrace.data.report.Report
import com.example.ubtrace.domain.report.ReportViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FoundedScreen(context : Context) {
    val reportViewModel = ReportViewModel()
    val auth = FirebaseAuth.getInstance()
    var userUid by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    val reports = remember { mutableStateOf<List<Report>>(emptyList()) }

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userUid = currentUser.uid
        } else {
            Toast.makeText(context, "No User is currently signed in", Toast.LENGTH_SHORT).show()
        }
        reportViewModel.getReportbyUserId(userUid = userUid, status = "Found", context = context) { data ->
            reports.value = data
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Nama Barang Diterima") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(reports.value.isEmpty()){
            Text(textAlign = TextAlign.Center, text = "TIDAK ADA REPORT")
        }
        else {
            LazyColumn (
            ) {
                items(reports.value) { report ->
                    ListItemCard(
                        title = report.item,
                        buttonText = "Sudah Ditemukan",
                        backgroundColorHex = 0xFFFF5722
                    )
                }
            }
        }
    }
}