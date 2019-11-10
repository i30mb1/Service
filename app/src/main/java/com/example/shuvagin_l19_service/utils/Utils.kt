package com.example.shuvagin_l19_service.utils

import android.content.Context
import android.os.StrictMode
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.example.shuvagin_l19_service.BuildConfig
import com.google.android.material.button.MaterialButtonToggleGroup

enum class Theme { LIGHT, DARK }

@BindingAdapter("changeTheme", "idFirstButton", "idSecondButton", requireAll = true)
fun MaterialButtonToggleGroup.changeTheme(theme: Theme?, idFirstButton: Int, idSecondButton: Int) {
    when (theme) {
        Theme.LIGHT -> {
            this.check(idFirstButton)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        Theme.DARK -> {
            this.check(idSecondButton)
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

fun permitDiskReads(func: () -> Any?): Any? {
    if (BuildConfig.DEBUG) {
        val oldThreadPolicy = StrictMode.getThreadPolicy()
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder(oldThreadPolicy)
                .permitDiskReads().build()
        )
        val anyValue = func()
        StrictMode.setThreadPolicy(oldThreadPolicy)

        return anyValue
    } else {
        return func()
    }
}

fun enableStrictMode() {
    if (BuildConfig.DEBUG) {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskWrites()
                .detectNetwork()
                .detectCustomSlowCalls()
                .detectResourceMismatches()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}
