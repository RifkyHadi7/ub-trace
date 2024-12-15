package com.example.ubtrace.presentation.Homepage.Components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ubtrace.domain.report.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(itemId: String?, navController: NavController) {
    val context = LocalContext.current
    val reportViewModel = ReportViewModel()
    var item by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var noTelp by remember { mutableStateOf("") }

    // Fetch reports on launch
    LaunchedEffect(Unit) {
        if (itemId != null) {
            reportViewModel.getReportbyId(context, reportId = itemId) { data ->
                item = data.item
                description = data.description
                noTelp = formatPhoneNumber(data.noTelp)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Detail Item", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Item: $item")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: $description")

            // WhatsApp Chat Button
            Button(onClick = {
                if (noTelp.isEmpty()) {
                    Toast.makeText(context, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val formattedNumber = if (noTelp.startsWith("62")) noTelp else "62${noTelp.trimStart('0')}"
                val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Halo, saya menemukan barang anda, berupa ${item}")
                    putExtra("jid", "$formattedNumber@s.whatsapp.net") // Format JID untuk WhatsApp
                    setPackage("com.whatsapp") // Pastikan intent diarahkan ke WhatsApp
                }

                try {
                    context.startActivity(whatsappIntent)
                } catch (e: Exception) {
                    Toast.makeText(context, "WhatsApp tidak ditemukan di perangkat ini", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Chat via WhatsApp")
            }
        }
    }
}

fun formatPhoneNumber(phoneNumber: String): String {
    return if (phoneNumber.startsWith("0")) {
        "62" + phoneNumber.drop(1) // Replace the first '0' with '62'
    } else if (!phoneNumber.startsWith("62")) {
        "62$phoneNumber" // Prepend '62' if not already present
    } else {
        phoneNumber // Return the original number if it already starts with '62'
    }
}