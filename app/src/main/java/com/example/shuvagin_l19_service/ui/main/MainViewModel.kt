package com.example.shuvagin_l19_service.ui.main

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val state: SavedStateHandle) : ViewModel() {

     val text:LiveData<String> = liveData(Dispatchers.IO) {
          emit("ready")
     }

     var isLightTheme: MutableLiveData<Boolean> = state.getLiveData("theme", true)

     fun test() {
          viewModelScope.launch {  }
     }
}
