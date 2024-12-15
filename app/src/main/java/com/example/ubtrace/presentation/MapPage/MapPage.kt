package com.example.ubtrace.presentation.MapPage

import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.ubtrace.data.report.Report
import com.example.ubtrace.domain.report.ReportViewModel
import com.example.ubtrace.presentation.util.components.Bottombar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(navController: NavController, reportViewModel: ReportViewModel) {
    val context = LocalContext.current
    val reports = remember { mutableStateOf<List<Report>>(emptyList()) }
    val mapView = MapView(context)
    val selectedReport = remember { mutableStateOf<Report?>(null) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        reportViewModel.getReport(context) { fetchedReports ->
            reports.value = fetchedReports
        }
        mapView.onCreate(Bundle())
        mapView.onResume()

        mapView.getMapAsync { googleMap ->
            reports.value.forEach { report ->
                val location = LatLng(report.latitude, report.longitude)
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(report.item)
                )
                marker?.tag = report
            }

            googleMap.setOnMarkerClickListener { marker ->
                val report = marker.tag as? Report
                selectedReport.value = report

                // Buka bottom sheet jika ada report yang dipilih
                coroutineScope.launch {
                    if (report != null) {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
                true
            }

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(-7.952586062167603, 112.61445825692978),
                    17f
                )
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState, // Gunakan scaffoldState untuk kontrol
        sheetContent = {
            selectedReport.value?.let { report ->
                BottomSheetContent(report = report)
            } ?: Text("Select a marker to see details")
        },
        sheetPeekHeight = 0.dp, // Awalnya tersembunyi
        content = {
            Scaffold(
                bottomBar = { Bottombar(navController) },
                content = { paddingValues ->
                    AndroidView(
                        modifier = Modifier.padding(paddingValues),
                        factory = { mapView }
                    )
                }
            )
        }
    )
}