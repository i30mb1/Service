package com.example.shuvagin_l19_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.text.SpannableStringBuilder
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
import java.io.File

class FileSaveService : Service() {
    private val FILE_NAME = "log"
    private val FILE_PATH by lazy {
        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    }
    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder = binder

    inner class LocalBinder : Binder() {

        fun getService(): FileSaveService = this@FileSaveService
    }

    fun save(text: SpannableStringBuilder) {
        File(FILE_PATH, FILE_NAME).appendBytes(HtmlCompat.toHtml(text, TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).toByteArray())
    }

    fun read(): String = File(FILE_PATH, FILE_NAME).takeIf {
        it.exists()
    }?.readText() ?: ""

    fun deleteAll() {
        File(FILE_PATH, FILE_NAME).writeText("")
    }
}
