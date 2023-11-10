package com.minthanhtike.permissionhandling

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class PermissionViewmodel(private val permissions:PermissionManager) : ViewModel() {
    val uiState = permissions.state

    fun onPermissionChange(requestedPermissions: Map<String, Boolean>) {
        permissions.onPermissionChange(requestedPermissions)
    }

    fun createSettingsIntent(): Intent {
        return permissions.createSettingsIntent()
    }

}

class PermissionViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app =
            extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PermissionHandlingApps
        return PermissionViewmodel(app.permissions) as T
    }
}