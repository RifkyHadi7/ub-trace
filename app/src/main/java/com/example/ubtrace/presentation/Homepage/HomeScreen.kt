package com.example.ubtrace.presentation.Homepage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.ubtrace.data.report.Report
import com.example.ubtrace.domain.report.ReportViewModel
import com.example.ubtrace.presentation.Homepage.Components.ItemCard
import com.example.ubtrace.presentation.Homepage.Components.Loading
import com.example.ubtrace.presentation.Homepage.Components.topAppbarHome
import com.example.ubtrace.presentation.util.components.Bottombar

@Composable
internal fun HomeScreen(navController: NavController, reportViewModel: ReportViewModel) {
    val reports = remember { mutableStateOf<List<Report>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Fetch reports on launch
    LaunchedEffect(Unit) {
        reportViewModel.getReport(context) { fetchedReports ->
            reports.value = fetchedReports
            isLoading.value = false
        }
    }

    HomeContent(
        isLoading = isLoading.value,
        reports = reports.value,
        navController = navController
    )
}

@Composable
fun HomeContent(
    isLoading: Boolean,
    reports: List<Report>,
    navController: NavController
) {
    Loading(isLoading = isLoading)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            topAppbarHome(title = "HALAMAN")
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(reports) { report ->
                    ItemCard(report)
                }
            }
        },
        bottomBar = {
            Bottombar(navController = navController)
        }
    )
}

//@Composable
//@Preview
//fun preview(){
//    HomeContent(state = HomeViewState())
//}