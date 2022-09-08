package com.bumian.memorynotes.common

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkPermissions(
    permissions: List<String>
): Boolean {
    var areAllGranted = true
    permissions.forEach {
        areAllGranted = ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        if(!areAllGranted) return@forEach
    }

    return areAllGranted

}