package com.rohithkadaveru.yelpProto.domain.model

import com.rohithkadaveru.yelpProto.domain.util.DomainMapper
import com.rohithkadaveru.yelpProto.network.model.BusinessSearchDto

class BusinessMapper : DomainMapper<BusinessSearchDto.Business, Business> {

    /**
     * maps given
     * @param BusinessSearchDto.Business model to [Business]
     */
    override fun mapToDomainModel(model: BusinessSearchDto.Business): Business {
        return Business(
            name = model.name,
            id = model.id,
            imageUrl = model.imageUrl,
            isClosed = model.isClosed,
            url = model.url,
            reviewCount = model.reviewCount ?: 0,
            rating = model.rating ?: 0.0,
            price = model.price ?: "N/A",
            phone = model.phone,
            distance = model.distance,
            address = model.location?.displayAddress?.joinToString() ?: "N/A",
            latitude = model.coordinates.latitude,
            longitude = model.coordinates.longitude
        )
    }

    /**
     * maps a list of [BusinessSearchDto.Business] to list of [Business]
     * @see mapToDomainModel
     */
    fun fromNetworkList(businessList: List<BusinessSearchDto.Business?>?): List<Business>? {
        return businessList?.filterNotNull()?.map { mapToDomainModel(it) }
    }


}