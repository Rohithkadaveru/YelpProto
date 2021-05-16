package com.rohithkadaveru.yelpProto.network

import com.rohithkadaveru.yelpProto.network.model.BusinessSearchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("businesses/search")
    suspend fun getBusinesses(
        @Query("term") term: String?,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Response<BusinessSearchDto>
}