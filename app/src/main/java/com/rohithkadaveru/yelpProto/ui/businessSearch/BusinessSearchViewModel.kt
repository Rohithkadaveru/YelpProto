package com.rohithkadaveru.yelpProto.ui.businessSearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohithkadaveru.yelpProto.domain.model.Business
import com.rohithkadaveru.yelpProto.domain.model.BusinessMapper
import com.rohithkadaveru.yelpProto.repository.Repository
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class BusinessSearchViewModel : ViewModel() {

    // default lat long to california
    var userLatitude: Double = 37.786882
    var userLongitude: Double = -122.399972

    // backing field to prevent exposing MutableLiveData
    private val _business: MutableLiveData<List<Business>> = MutableLiveData()
    val business: LiveData<List<Business>> = _business

    // TODO use singleLiveEvent or Flow
    private val _error: MutableLiveData<Boolean> = MutableLiveData()
    val error: LiveData<Boolean> = _error

    /**
     * this method launches a coroutine scope to make a suspend function call to fetch business for
     * @param searchTerm String. Result will be saved in LiveData to be observed from view layer
     */
    fun loadBusinesses(searchTerm: String?) {
        viewModelScope.launch {
            try {
                val response = Repository.getBusinesses(
                    searchTerm,
                    userLatitude.toString(),
                    userLongitude.toString()
                )

                if (response.isSuccessful) {
                    // TODO mapping in repository instead
                    val result = BusinessMapper().fromNetworkList(response.body()?.businesses)
                    _business.value = result
                } else {
//                response.code()
                    // TODO throw error with details
                    _error.value = true
                }
            }
            // FIXME having a sealed Result class would help with handling exceptions at single place
            catch (e: UnknownHostException) {
                _error.value = true
            } catch (e: IOException) {
                _error.value = true
            }
        }
    }
}