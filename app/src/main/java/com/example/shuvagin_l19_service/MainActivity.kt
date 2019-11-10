package com.example.shuvagin_l19_service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shuvagin_l19_service.ui.main.MainFragment
import com.example.shuvagin_l19_service.utils.enableStrictMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableStrictMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}
