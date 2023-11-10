package com.minthanhtike.permissionhandling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.minthanhtike.permissionhandling.ui.theme.PermissionHandlingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = (application as PermissionHandlingApps).permissions

        setContent {
            PermissionHandlingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
//                    val startNavigation =
//                        if (permissionManager.hasAllPermissions) {
//                            Screens.Home.route
//                        } else {
//                            Screens.Permissions.route
//                        }
                    NavHost(
                        navController = navController,
                        startDestination = Screens.AddLog.route
                    ) {
                        composable(Screens.Permissions.route) {
                            PermissionScreen(
                                navController
                            )
                        }
                        composable(Screens.Home.route) { HomeScreen() }
                        composable(Screens.AddLog.route) { AddLogScreen(navController) }
                        composable(Screens.Camera.route) { CameraScn(navController) }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            permissionManager.checkPermissions()
        }
    }
}

sealed class Screens(val route: String) {
    object Permissions : Screens("permissions")
    object Home : Screens("home")
    object AddLog : Screens("add_log")
    object Camera : Screens("camera")
}
