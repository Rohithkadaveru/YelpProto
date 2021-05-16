package com.rohithkadaveru.yelpProto.util

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.rohithkadaveru.yelpProto.R

object DialogUtils {
    fun showRequestPermissionDialog(fragment: Fragment) {
        val builder = AlertDialog.Builder(fragment.requireContext())

        builder.apply {
            setTitle(context.getString(R.string.location_permission_required))
            setMessage(context.getString(R.string.location_permission_required_message))
            setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 111)
            }
            setCancelable(false)
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun showRationaleDialog(fragment: Fragment) {
        val builder = AlertDialog.Builder(fragment.requireContext())

        builder.apply {
            setTitle(context.getString(R.string.location_permission_required))
            setMessage(context.getString(R.string.location_permission_required_message))
            setPositiveButton(context.getString(R.string.settings)) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                fragment.startActivity(intent)
            }
            setCancelable(false)
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun showNoBusinessesAvailable(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.apply {
            setTitle(context.getString(R.string.no_businesses_found))
            setMessage(context.getString(R.string.try_different_word))
            setPositiveButton(context.getString(R.string.ok)) { _, _ ->

            }
            setCancelable(false)
        }
        val dialog = builder.create()
        dialog.show()

    }

    fun showGenericError(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.apply {
            setTitle(context.getString(R.string.no_businesses_found))
            setMessage(context.getString(R.string.try_different_word))
            setPositiveButton(context.getString(R.string.ok)) { _, _ ->

            }
            setCancelable(false)
        }
        val dialog = builder.create()
        dialog.show()

    }
}