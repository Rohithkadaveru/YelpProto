package com.rohithkadaveru.yelpProto.domain.util

/**
 * is used to map T to DomainModel or viceversa
 */
interface DomainMapper <T, DomainModel> {

    /**
     * maps given
     * @param model to [DomainModel]
     */
    fun mapToDomainModel(model: T): DomainModel

//    unused as we are not posting anything
//    fun mapToNetworkModel(networkModel: DomainModel): T
}