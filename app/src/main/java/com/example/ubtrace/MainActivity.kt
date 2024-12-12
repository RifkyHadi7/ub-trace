package com.example.ubtrace

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ubtrace.domain.report.ReportViewModel
import com.example.ubtrace.domain.user.UserViewModel
import com.example.ubtrace.presentation.Homepage.HomeScreen
import com.example.ubtrace.presentation.LoginScreen.LoginScreen
import com.example.ubtrace.presentation.ReportScreen.ReportScreen
import com.example.ubtrace.presentation.SignInScreen.SignInScreen
import com.example.ubtrace.presentation.WelcomeScreen.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private var hasLocationPermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissisions()){
            ActivityCompat.requestPermissions(
                this, LOCATION_PERMISSIONS, 0
            )
        }
        setContent {
            AppNavigation()
        }
    }
    private fun hasRequiredPermissisions(): Boolean {
        return LOCATION_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    companion object{
        private val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val reportViewModel = ReportViewModel()
    val userViewModel = UserViewModel()

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) "home" else "welcome"

    ) {
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("login") {
            LoginScreen(navController) {
                navController.navigate("home")
            }
        }
        composable("signup") {
            SignInScreen(navController) {
                navController.navigate("login")
            }
        }
        composable("home") {
            HomeScreen(navController = navController, reportViewModel = reportViewModel)
        }
        composable("report") {
            ReportScreen(navController = navController, reportViewModel = reportViewModel, userViewModel = userViewModel)
        }
    }
}