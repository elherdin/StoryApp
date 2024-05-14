package com.example.intermediatesub.di

import android.content.Context
import com.example.intermediatesub.data.pref.UserPreference
import com.example.intermediatesub.data.pref.dataStore
import com.example.intermediatesub.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        return Repository.getInstance(pref)
    }
}