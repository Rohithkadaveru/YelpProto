package com.rohithkadaveru.yelpProto.ui.businessDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rohithkadaveru.yelpProto.R
import com.rohithkadaveru.yelpProto.databinding.BusinessDetailFragmentBinding
import com.rohithkadaveru.yelpProto.util.DialogUtils
import com.rohithkadaveru.yelpProto.util.FragmentPermissions
import com.rohithkadaveru.yelpProto.util.extensions.toMiles

class BusinessDetailFragment : Fragment() {

    private lateinit var viewModel: BusinessDetailViewModel
    private lateinit var binding: BusinessDetailFragmentBinding

    // TODO make this reactive to permission updates
    private var isLocationAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BusinessDetailViewModel::class.java)
        binding = BusinessDetailFragmentBinding.inflate(inflater, container, false)

        viewModel.business = BusinessDetailFragmentArgs.fromBundle(requireArguments()).business

        setupBindings()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        return binding.root
    }

    private fun setupBindings() {

        val business = viewModel.business

        Glide.with(this)
            .load(viewModel.business.imageUrl)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.image)

        with(binding) {

            businessName.text = business.name

            callBtn.setOnClickListener {
                // TODO check if the device can handle the uri before starting the activity
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${viewModel.business.phone}")
                context?.startActivity(intent)
            }

            websiteBtn.setOnClickListener {
                // TODO check if the device can handle the uri before starting the activity
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(viewModel.business.url)
                context?.startActivity(intent)
            }

            ratingBar.rating = business.rating.toFloat()

            reviewsLabel.text = "${business.reviewCount} Reviews"

            // update currents status text and color based on whether the business is currently open or not
            currentStatusLabel.text = if (business.isClosed) {
                "Closed"
            } else {
                "Open"
            }
            currentStatusLabel.setTextColor(
                if (business.isClosed) {
                    R.color.red
                } else {
                    R.color.green
                }
            )

            val address =
                business.address + "(${business.distance.toMiles()} away)"
            addressLabel.text = address

            directionsBtn.setOnClickListener {
                // TODO check if the device can handle the uri before starting the activity
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=${business.latitude},${business.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val callback =
        OnMapReadyCallback { googleMap ->
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */

            with(googleMap.uiSettings) {
                isMyLocationButtonEnabled = isLocationAvailable
                isZoomControlsEnabled = true
            }

            val business = viewModel.business

            val businessMarker =
                LatLng(business.latitude, business.longitude)

            googleMap.addMarker(MarkerOptions().position(businessMarker).title(business.name))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(businessMarker, 15.5f))
            googleMap.isMyLocationEnabled = isLocationAvailable
        }

    // region permissions
    override fun onResume() {
        super.onResume()

        FragmentPermissions.handlePermissions(this, object : FragmentPermissions.PermissionResultListener {
            override fun onPermissionGranted() {
                isLocationAvailable = true
            }

            override fun onPermissionDenied() {
                isLocationAvailable = false
                DialogUtils.showRequestPermissionDialog(this@BusinessDetailFragment)
            }

            override fun onPermissionRationale() {
                isLocationAvailable = false
                DialogUtils.showRationaleDialog(this@BusinessDetailFragment)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            111 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // We can use the Location API now that the permission is granted
                    Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT)
                        .show()
                    isLocationAvailable = true
                } else {
                    DialogUtils.showRationaleDialog(this)
                }
                return
            }

            // Add other 'when' lines to if we need more to add more permissions later
            else -> {
                // Ignore all other requests.
            }
        }
    }
    // endregion
}