package com.example.shuvagin_l19_service.utils

import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

@BindingAdapter("changeTheme")
fun MaterialButtonToggleGroup.changeTheme(isLightTheme: Boolean) {
    when (isLightTheme) {
        true -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        false -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}

@BindingAdapter("isSelectedMy")
fun MaterialButton.setSelectedForMaterialButton(selected: Boolean) {
    this.isSelected = selected
}