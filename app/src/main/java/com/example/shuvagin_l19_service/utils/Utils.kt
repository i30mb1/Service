package com.example.shuvagin_l19_service.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButtonToggleGroup

enum class Theme { LIGHT, DARK }

@BindingAdapter("changeTheme")
fun MaterialButtonToggleGroup.changeTheme(theme: Theme) {
    when (theme) {
        Theme.LIGHT -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Theme.DARK -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}

fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}
