package com.example.ubtrace.presentation.ReportScreen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double
) {
    val context = LocalContext.current
    val mapView = MapView(context)
    mapView.onCreate(Bundle())
    mapView.onResume()

    AndroidView(modifier = modifier,factory = { mapView })

    mapView.getMapAsync { googleMap ->
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker at ($latitude, $longitude)"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}