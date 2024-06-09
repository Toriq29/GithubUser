package com.thoriq.githubuser.data.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thoriq.githubuser.ui.MainViewModel

class ViewModelDarkModeVactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}