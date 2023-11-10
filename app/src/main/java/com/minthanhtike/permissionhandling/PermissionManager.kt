package com.minthanhtike.permissionhandling

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionManager(private val context: Context) {
    companion object{
        val PERMISSION_PRE_T= arrayOf(
            READ_EXTERNAL_STORAGE,
            CAMERA,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )

        val PERMISSION_POST_T= arrayOf(
            READ_MEDIA_IMAGES,
            CAMERA,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
    }
    data class State(
        val hasStorageAccess:Boolean,
        val hasCameraAccess:Boolean,
        val hasLocationAccess:Boolean
    ){
        val hasAllAccess:Boolean
            get() = hasStorageAccess and hasCameraAccess and hasLocationAccess
    }

    private fun hasEachItemAccess(permission:String):Boolean{
        return ContextCompat.checkSelfPermission(
            context,permission
        )==PackageManager.PERMISSION_GRANTED
    }

    private fun hasMultiAccess(permission: List<String>):Boolean{
        return permission.all(::hasEachItemAccess)
    }


    private val _state= MutableStateFlow(
        State(
            hasCameraAccess = hasEachItemAccess(CAMERA),
            hasStorageAccess = hasEachItemAccess(Manifest.permission.READ_MEDIA_IMAGES)
            or hasEachItemAccess(Manifest.permission.READ_EXTERNAL_STORAGE),
            hasLocationAccess = hasMultiAccess(listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
        )
    )

    val state= _state.asStateFlow()
    val hasAllPermissions : Boolean
        get() =_state.value.hasAllAccess

    fun onPermissionChange(permission:Map<String,Boolean>){
        val hasLocationAccess = hasEachItemAccess(ACCESS_FINE_LOCATION) &&
                hasEachItemAccess(ACCESS_COARSE_LOCATION)
        val hasStorageAccess = hasEachItemAccess(READ_MEDIA_IMAGES) ||
                hasEachItemAccess(READ_EXTERNAL_STORAGE)

        _state.value= State(
            hasStorageAccess = hasStorageAccess,
            hasCameraAccess = permission[CAMERA] ?: _state.value.hasCameraAccess,
            hasLocationAccess = hasLocationAccess
        )
    }

    suspend fun checkPermissions() {
        val newState = State(
            hasStorageAccess = hasEachItemAccess(READ_EXTERNAL_STORAGE)
                    || hasEachItemAccess(READ_MEDIA_IMAGES),
            hasCameraAccess = hasEachItemAccess(CAMERA),
            hasLocationAccess = hasEachItemAccess(ACCESS_FINE_LOCATION) &&
                    hasEachItemAccess(ACCESS_COARSE_LOCATION)
        )

        _state.emit(newState)
    }

    fun createSettingsIntent(): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", context.packageName, null)
        }

        return intent
    }
}