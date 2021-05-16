package com.rohithkadaveru.yelpProto.util.extensions

fun Double.toMiles(): String {
    return "%.2f".format(this.times(0.000621371192))
}