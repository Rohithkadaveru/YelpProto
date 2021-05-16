package com.rohithkadaveru.yelpProto.network

import com.rohithkadaveru.yelpProto.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://api.yelp.com/v3/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Interceptor OKHTTP3
    fun okHttpClient(): OkHttpClient {

        val okhttpInterceptor = HttpLoggingInterceptor()

        okhttpInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(30, TimeUnit.SECONDS)

        okHttpClient.addInterceptor { chain ->
            val original = chain.request()

            // TODO extract key to properties file
            val request = original.newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${BuildConfig.YELP_API_KEY}"
                )
                .build()
            chain.proceed(request)
        }
        okHttpClient.addInterceptor(okhttpInterceptor)

        return okHttpClient.build()
    }
}