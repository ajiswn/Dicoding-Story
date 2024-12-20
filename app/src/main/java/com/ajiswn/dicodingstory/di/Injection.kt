package com.ajiswn.dicodingstory.di

import android.content.Context
import com.ajiswn.dicodingstory.data.Repository
import com.ajiswn.dicodingstory.data.pref.UserPreference
import com.ajiswn.dicodingstory.data.pref.dataStore
import com.ajiswn.dicodingstory.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}