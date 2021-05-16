package com.rohithkadaveru.yelpProto.util

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class FragmentPermissions {
    companion object {
        private val TAG = FragmentPermissions::class.java.canonicalName

        fun handlePermissions(fragment: Fragment, permissionResultListener: PermissionResultListener) {
            when {
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // We can use the Location API now that the permission is granted
                    Log.d(TAG, "Permission has been granted already")
                    permissionResultListener.onPermissionGranted()
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Log.d(TAG, "Showing Rationale")
                    permissionResultListener.onPermissionRationale()
                }
                else -> {
                    permissionResultListener.onPermissionDenied()
                }
            }
        }
    }

    // TODO pass as lambda
    interface PermissionResultListener {
        fun onPermissionGranted()
        fun onPermissionDenied()
        fun onPermissionRationale()
    }
}