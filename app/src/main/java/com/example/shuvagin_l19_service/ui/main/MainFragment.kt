package com.example.shuvagin_l19_service.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import com.example.shuvagin_l19_service.LogServiceHelper
import com.example.shuvagin_l19_service.R
import com.example.shuvagin_l19_service.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val logService by lazy { LogServiceHelper(requireActivity(), lifecycleScope) { setTextFromLog(it) } }
    private val viewModel: MainViewModel by viewModels { SavedStateViewModelFactory(requireActivity().application, this) }
    private lateinit var binding: MainFragmentBinding

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
        lifecycle.addObserver(logService)
        setDefaultValues()
        binding.bMainFragmentReadLogs.setOnLongClickListener { v: View? -> deleteLog(); readLog(); true }
    }

    private fun setDefaultValues() {
        viewModel.isLightTheme.value?.let {
            binding.bDarkTheme.isChecked = !it
            binding.bLightTheme.isChecked = it
        }
    }

    fun saveLog(v: View) {
        logService.saveLog((v as TextView).text.toString())
    }

    fun readLog() {
        logService.readLog()
    }

    private fun deleteLog() {
        logService.deleteLog()
    }

    private fun setTextFromLog(text: String) {
        lifecycleScope.launchWhenResumed {
            val textMetricsParams = TextViewCompat.getTextMetricsParams(binding.tvMainFragmentContainer)
            val precomputedText = withContext(Dispatchers.Default) {
                val hmtlText = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
                PrecomputedTextCompat.create(hmtlText, textMetricsParams)
            }
            TextViewCompat.setPrecomputedText(binding.tvMainFragmentContainer, precomputedText)
            delay(100)
            scroll_main_fragment.fullScroll(View.FOCUS_DOWN)
        }
    }

    fun setLightTheme(lightTheme: Boolean) {
        viewModel.isLightTheme.value = lightTheme
    }

}
