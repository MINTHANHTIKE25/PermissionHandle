package com.minthanhtike.permissionhandling

import android.app.Application

class PermissionHandlingApps : Application() {
    lateinit var permissions: PermissionManager

    override fun onCreate() {
        super.onCreate()

        permissions = PermissionManager(this)
    }
}