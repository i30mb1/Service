package com.example.shuvagin_l19_service.ui.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.collection.arraySetOf
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
import androidx.core.text.PrecomputedTextCompat
import androidx.core.text.color
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shuvagin_l19_service.FileSaveService
import com.example.shuvagin_l19_service.R
import com.example.shuvagin_l19_service.databinding.MainFragmentBinding
import com.example.shuvagin_l19_service.utils.getColorFromAttr
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding
    lateinit var fileSaveService: FileSaveService
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? FileSaveService.LocalBinder
            binder?.let {
                fileSaveService = binder.getService()
                bound = true
                readLog()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.viewModel = viewModel
        binding.fragment = this@MainFragment
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setDefaultValues()
        binding.bMainFragmentReadLogs.setOnLongClickListener { v: View? -> deleteLog(); readLog(); true }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext(), FileSaveService::class.java)
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unbindService(connection)
    }

    private fun setDefaultValues() {
        viewModel.isLightTheme.value?.let {
            binding.bDarkTheme.isChecked = !it
            binding.bLightTheme.isChecked = it
        }
    }

    @SuppressLint("SetTextI18n", "RestrictedApi")
    fun saveLog(v: View) {
        if (!bound) return

        val textFile = SpannableStringBuilder().append("PRESSED ")
            .color(requireContext().getColorFromAttr(R.attr.colorPrimary)) { append("\"") }
            .append((v as TextView).text)
            .color(requireContext().getColorFromAttr(R.attr.colorPrimary)) { append("\"\n") }

        arraySetOf<Int>() + arraySetOf<Int>()

        fileSaveService.save(textFile)
    }

    fun readLog() {
        if (!bound) return
        lifecycleScope.launchWhenResumed {
            val textMetricsParams = TextViewCompat.getTextMetricsParams(binding.tvMainFragmentContainer)
            val precomputedText = withContext(Dispatchers.Default){
                val text = HtmlCompat.fromHtml(fileSaveService.read(), FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
                PrecomputedTextCompat.create(text,textMetricsParams)
            }
            TextViewCompat.setPrecomputedText(binding.tvMainFragmentContainer,precomputedText)
            delay(100)
            scroll_main_fragment.fullScroll(View.FOCUS_DOWN)
        }

    }

    fun deleteLog() {
        if (bound) {
            fileSaveService.deleteAll()
        }
    }

    fun setLightTheme(lightTheme: Boolean) {
        viewModel.isLightTheme.value = lightTheme
    }
}
