package com.example.shuvagin_l19_service.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shuvagin_l19_service.utils.Theme

class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

    val isLightTheme: MutableLiveData<Theme> = state.getLiveData("theme", Theme.LIGHT)

    fun setLightTheme(theme: Theme) {
        isLightTheme.value = theme
    }

}
