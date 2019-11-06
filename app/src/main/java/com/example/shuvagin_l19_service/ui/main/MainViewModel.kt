package com.example.shuvagin_l19_service.ui.main

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

     val text:LiveData<String> = liveData(Dispatchers.IO) {
          emit("ready")
     }
     val isLightTheme = MutableLiveData<Boolean>(true)

     fun test() {
          viewModelScope.launch {  }
     }
}
