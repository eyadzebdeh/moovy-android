package com.apptasya.moovy.di

import android.content.Context
import android.provider.Settings
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
): Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = addAdditionalHeaders(chain.request())
        return chain.proceed(request)
    }

    private fun addAdditionalHeaders(request: Request): Request {
        val builder = request.newBuilder()
        builder.header("deviceid", getDeviceId())
        return builder.build()
    }

    private fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}