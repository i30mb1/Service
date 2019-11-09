package com.example.shuvagin_l19_service

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.text.SpannableStringBuilder
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
import androidx.core.text.color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.shuvagin_l19_service.utils.getColorFromAttr
import java.io.File

class LogService
    : Service() {
    private val FILE_NAME = "log"
    private val FILE_PATH by lazy {
        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    }
    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class LocalBinder : Binder() {

        fun getService(): LogService = this@LogService
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

interface OnNewLogListener {
    fun newLog(): String
}

class LogServiceHelper(private val activity: Activity, private val lifecycleScope: LifecycleCoroutineScope, val callBack: (text: String) -> Unit) : LifecycleObserver {
    lateinit var fileSaveService: LogService
    private var bound = false
    var OnNewLogListener: OnNewLogListener? = null
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? LogService.LocalBinder
            binder?.let {
                fileSaveService = binder.getService()
                bound = true
                readLog()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bindService() {
        val intent = Intent(activity, LogService::class.java)
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbindService() {
        activity.unbindService(connection)
    }

    fun saveLog(text: String) {
        if (!bound) return

        val textFile = SpannableStringBuilder().append("PRESSED ")
            .color(activity.getColorFromAttr(R.attr.colorPrimary)) { append("\"") }
            .append(text)
            .color(activity.getColorFromAttr(R.attr.colorPrimary)) { append("\"\n") }

        fileSaveService.save(textFile)
    }

    fun deleteLog() {
        if (bound) {
            fileSaveService.deleteAll()
        }
    }

    fun readLog() {
        if (bound) {
            val textFromServer = fileSaveService.read()
            callBack.invoke(textFromServer)
        }
    }

}

