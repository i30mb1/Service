package com.example.shuvagin_l19_service.utils

import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter

@BindingAdapter("onChecked")
fun Switch.onChecked() {
    when (this.isChecked) {
        true -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        false -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}