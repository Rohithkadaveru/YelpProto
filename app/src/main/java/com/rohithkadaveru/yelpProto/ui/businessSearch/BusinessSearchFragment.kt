package com.rohithkadaveru.yelpProto.ui.businessSearch

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rohithkadaveru.yelpProto.R
import com.rohithkadaveru.yelpProto.databinding.BusinessSearchFragmentBinding
import com.rohithkadaveru.yelpProto.domain.model.Business
import com.rohithkadaveru.yelpProto.util.DialogUtils
import com.rohithkadaveru.yelpProto.util.FragmentPermissions

class BusinessSearchFragment : Fragment() {

    private val TAG = BusinessSearchFragment::class.java.canonicalName

    private lateinit var viewModel: BusinessSearchViewModel
    private lateinit var binding: BusinessSearchFragmentBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val adapter by lazy { BusinessRecyclerViewAdapter { business -> adapterOnClick(business) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BusinessSearchViewModel::class.java)
        binding = BusinessSearchFragmentBinding.inflate(inflater, container, false)

        setupBinding()
        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.business.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            Log.d("Search", it.toString())
            if (it.isNullOrEmpty()) {
                DialogUtils.showNoBusinessesAvailable(requireContext())
            } else {
                adapter.updateBusinessList(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (it) {
                // FIXME: will hold the previous error. Use singleLiveEvent or Flow
                DialogUtils.showGenericError(requireContext())
            }
        }
    }

    private fun setupBinding() {
        with(binding) {
            searchText.setEndIconOnClickListener {
                onSearchClicked(searchText.editText?.text.toString())
            }

            // intercept Enter/Search button on the keyboard to kick off search action
            searchText.editText?.setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        onSearchClicked(searchText.editText?.text.toString())
                        true
                    }
                    else -> false
                }
            }

            businessList.layoutManager = LinearLayoutManager(context)
            businessList.adapter = adapter
        }
    }

    /**
     * @param searchTerm term to search businesses with
     *
     * triggers business list fetch from viewModel
     * shows a progress bar and hides the keyboard
     */
    private fun onSearchClicked(searchTerm: String?) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.loadBusinesses(searchTerm)
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchText.windowToken, 0)
    }

    /* Opens BusinessDetailFragment when RecyclerView item is clicked. */
    private fun adapterOnClick(business: Business) {
        val action =
            BusinessSearchFragmentDirections.actionSearchFragmentToBusinessDetailFragment(business)
        findNavController().navigate(action)
    }

    @SuppressLint("MissingPermission")
    private fun getFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            // Got last known location. In some rare situations this can be null.
            if (location == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.location_default_ca),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.userLatitude = location.latitude
                viewModel.userLongitude = location.longitude
            }
        }
    }

    // region permissions
    override fun onResume() {
        super.onResume()

        // CheckPermissions in onResume as users can disable permissions from Settings screen
        // even after granting them in the Application
        FragmentPermissions.handlePermissions(this, object : FragmentPermissions.PermissionResultListener {
            override fun onPermissionGranted() {
                getFusedLocationClient()
            }

            override fun onPermissionDenied() {
                DialogUtils.showRequestPermissionDialog(this@BusinessSearchFragment)
            }

            override fun onPermissionRationale() {
                DialogUtils.showRationaleDialog(this@BusinessSearchFragment)
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
                    Toast.makeText(
                        context,
                        getString(R.string.location_permission_granted),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    getFusedLocationClient()
                } else {
                    Log.d(TAG, "permission denied")
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