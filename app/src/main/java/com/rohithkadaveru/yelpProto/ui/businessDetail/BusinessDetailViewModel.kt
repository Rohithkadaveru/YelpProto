package com.rohithkadaveru.yelpProto.ui.businessDetail

import androidx.lifecycle.ViewModel
import com.rohithkadaveru.yelpProto.domain.model.Business

class BusinessDetailViewModel : ViewModel() {
    /**
     * holds business item that user clicked on.
     * this is not a LiveData at this point as the data passed down and is immutable
     **/
    lateinit var business: Business
}