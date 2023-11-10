package com.minthanhtike.permissionhandling

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun CameraScn(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "This is camera.",
            modifier = Modifier.clickable {
                navController.navigate(Screens.Permissions.route)
            })
    }
}