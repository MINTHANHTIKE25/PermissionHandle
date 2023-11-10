package com.minthanhtike.permissionhandling

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minthanhtike.permissionhandling.PermissionManager.Companion.PERMISSION_POST_T
import com.minthanhtike.permissionhandling.PermissionManager.Companion.PERMISSION_PRE_T

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(navController: NavController) {
    val viewModel:PermissionViewmodel=viewModel(factory = PermissionViewModelFactory())
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var hasRequestedPermissions by remember { mutableStateOf(false) }

    val requestPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            hasRequestedPermissions = true
            viewModel.onPermissionChange(permissions)
        }

    fun openSettings() {
        ContextCompat.startActivity(context, viewModel.createSettingsIntent(), null)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Permissions needed", fontFamily = FontFamily.Serif) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "You have to grant access to these permissions in order to use the app"
            )
            ListItem(
                headlineText = { Text("Storage access") },
                supportingText = { Text("Add photos from library when creating a log") },
                trailingContent = { PermissionAccessIcon(state.value.hasStorageAccess) },
                leadingContent = {
                    Icon(
                        Icons.Filled.PhotoLibrary,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            )
            Divider()
            ListItem(
                headlineText = { Text("Camera access") },
                supportingText = { Text("Take picture when creating a log") },
                trailingContent = { PermissionAccessIcon(state.value.hasCameraAccess) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Camera,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            )
            Divider()
            ListItem(
                headlineText = { Text("Precise location access") },
                supportingText = { Text("Keep track of the location of a log") },
                trailingContent = { PermissionAccessIcon(state.value.hasLocationAccess) },
                leadingContent = {
                    Icon(
                        Icons.Filled.Explore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            )
            Spacer(Modifier.height(32.dp))
            if (state.value.hasAllAccess) {
                FilledTonalButton(onClick = { navController.navigate(Screens.Home.route) }) {
                    Text("Get started")
                }
            } else {
                if (hasRequestedPermissions) {
                    FilledTonalButton(onClick = { openSettings() }) {
                        Text("Go to settings")
                    }
                } else {
                    FilledTonalButton(onClick = {
                        if (Build.VERSION.SDK_INT >= 33) {
                            requestPermissions.launch(PERMISSION_POST_T)
                        }
                        else {
                            requestPermissions.launch(PERMISSION_PRE_T)
                        }
                    }) {
                        Text("Request permissions")
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionAccessIcon(hasAccess: Boolean) {
    if (hasAccess) {
        Icon(
            Icons.Filled.Check,
            contentDescription = "Permission accepted"
        )
    } else {
        Icon(
            Icons.Filled.Close,
            contentDescription = "Permission not granted"
        )
    }
}