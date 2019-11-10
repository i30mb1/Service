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
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.shuvagin_l19_service.utils.getColorFromAttr
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

class LogService : Service() {

    private val fileName by lazy { getString(R.string.log_service_file_name) }
    private val filePath by lazy { getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) }
    private val binder = LocalBinder()
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    inner class LocalBinder : Binder() {
        fun getService(): LogService = this@LogService
    }

    fun saveText(text: SpannableStringBuilder) = serviceScope.launch {
        val textSpannable = HtmlCompat.toHtml(text, TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
        File(filePath, fileName).appendBytes(textSpannable.toByteArray())
    }

    fun deleteAll() = serviceScope.launch {
        File(filePath, fileName).writeText("")
    }

    suspend fun readText() = withContext(Dispatchers.IO) {
        val file = File(filePath, fileName).takeIf { it.exists() }
        file?.readText() ?: getString(R.string.log_service_cannot_read)
    }
}

class LogServiceHelper(private val activity: Activity) : LifecycleObserver, CoroutineScope {
    private var job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val helperScope = CoroutineScope(coroutineContext)
    private lateinit var fileSaveService: LogService
    private var serviceConnected: (() -> Unit)? = null
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? LogService.LocalBinder
            binder?.let {
                fileSaveService = binder.getService()
                bound = true
                serviceConnected?.invoke()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onDestroyJob() = job.cancel()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun bindService() {
        val intent = Intent(activity, LogService::class.java)
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun unbindService() {
        activity.unbindService(connection)
    }

    fun addServerConnectedListener(listener: () -> Unit) {
        serviceConnected = listener
    }

    fun saveLog(text: String) {
        if (bound) {
            val textFile = SpannableStringBuilder().append(activity.getString(R.string.pressed))
                .color(activity.getColorFromAttr(R.attr.colorPrimary)) { append(" \"") }
                .append(text)
                .color(activity.getColorFromAttr(R.attr.colorPrimary)) { append("\"\n") }

            fileSaveService.saveText(textFile)
        }
    }

    fun deleteLog() {
        if (bound) {
            fileSaveService.deleteAll()
        }
    }

    suspend fun readLog(): String {
        if (bound) {
            return fileSaveService.readText()
        }
        return activity.getString(R.string.log_service_helper_not_bound)
    }

}

