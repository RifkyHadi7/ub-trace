package com.example.ubtrace.presentation.ReportScreen

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ubtrace.data.report.Report
import com.example.ubtrace.domain.report.ReportViewModel
import com.example.ubtrace.domain.user.UserViewModel
import com.example.ubtrace.presentation.util.components.Bottombar
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun ReportScreen(
    navController: NavController,
    reportViewModel: ReportViewModel,
    userViewModel: UserViewModel,
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var userUid by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<String?>(null) }
    var longitude by remember { mutableStateOf<String?>(null) }
    var notelp by remember { mutableStateOf("") }
    var item by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
            }

            val currentUser = auth.currentUser
            if (currentUser != null) {
                userUid = currentUser.uid
                userViewModel.getNoTelpbyId(userUid = userUid, context = context) { data ->
                    notelp = data
                }
            } else {
                Toast.makeText(context, "No User is currently signed in", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to get location", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Bottombar(navController = navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            latitude?.let { lat ->
                                longitude?.let { lon ->
                                    MapScreen(
                                        modifier = Modifier.fillMaxWidth().height(200.dp),
                                        latitude = lat.toDouble(),
                                        longitude = lon.toDouble()
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = latitude ?: "", // Display empty if null
                            onValueChange = {},
                            label = { Text("Latitude") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = longitude ?: "", // Display empty if null
                            onValueChange = {},
                            label = { Text("Longitude") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = notelp,
                            onValueChange = { notelp = it },
                            label = { Text("Nomor Telepon") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = item,
                            onValueChange = { item = it },
                            label = { Text("Item") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Choose Image")
                        }

                        imageUri?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = "Selected Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = "Selected Image: ${it.lastPathSegment}", modifier = Modifier.padding(horizontal = 8.dp))
                        }
                    }
                }

                Button(
                    onClick = {
                        if (!latitude.isNullOrBlank() && !longitude.isNullOrBlank() &&
                            notelp.isNotBlank() && item.isNotBlank() &&
                            description.isNotBlank() && imageUri != null) {
                            val report = Report(
                                userUid = userUid,
                                latitude = latitude?.toDoubleOrNull() ?: 0.0,
                                longitude = longitude?.toDoubleOrNull() ?: 0.0,
                                item = item,
                                description = description
                            )

                            isLoading = true
                            reportViewModel.addReport(report, context, notelp, imageUri!!, onComplete = {
                                isLoading = false
                                navController.navigate("home")
                            })
                        } else {
                            Toast.makeText(context, "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(text = "Submit Report")
                }
            }
        }
    )
}
