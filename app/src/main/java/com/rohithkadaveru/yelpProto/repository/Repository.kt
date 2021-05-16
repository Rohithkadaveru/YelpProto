package com.rohithkadaveru.yelpProto.repository

import com.rohithkadaveru.yelpProto.network.ApiClient
import com.rohithkadaveru.yelpProto.network.model.BusinessSearchDto
import retrofit2.Response

object Repository {

    /**
     * is a suspend function to retrieve [BusinessSearchDto].
     * Currently gets the list from network. Can be expanded to handle other sources like in-memory caching, localDb etc.,
     * @param searchTerm Optional. Search term, for example "food" or "restaurants". The term may also be business names, such as "Starbucks".
     * @param latitude Latitude of the location you want to search nearby.
     * @param longitude Longitude of the location you want to search nearby.
     */
    // TODO return Flow instead
    // TODO use a sealed class for Result
    suspend fun getBusinesses(
        searchTerm: String?,
        latitude: String,
        longitude: String
    ): Response<BusinessSearchDto> {
        // TODO caching logic
        return ApiClient.api.getBusinesses(
            term = searchTerm,
            latitude = latitude,
            longitude = longitude
        )
    }
}