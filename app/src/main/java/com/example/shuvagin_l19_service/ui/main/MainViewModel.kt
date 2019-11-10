package com.example.shuvagin_l19_service.ui.main

import androidx.lifecycle.*
import com.example.shuvagin_l19_service.utils.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

    val text: LiveData<String> = liveData(Dispatchers.IO) {
        emit("ready")
    }

    val isLightTheme: MutableLiveData<Theme> = state.getLiveData("theme", Theme.LIGHT)

    fun setLightTheme(theme: Theme) {
        isLightTheme.value = theme
    }

    fun test() {
        viewModelScope.launch { }
    }
}
