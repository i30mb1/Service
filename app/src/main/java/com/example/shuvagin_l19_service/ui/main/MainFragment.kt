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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val logServiceHelper by lazy { LogServiceHelper(requireActivity()) }
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
        lifecycle.addObserver(logServiceHelper)
        binding.bMainFragmentReadLogs.setOnLongClickListener { v: View? ->
            deleteLog()
            readLog()
            true
        }
    }

//    private fun setDefaultValuesTheme() {
//        viewModel.isLightTheme.value?.let {
//            binding.bDarkTheme.isChecked = !it
//            binding.bLightTheme.isChecked = it
//        }
//    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            binding.bLightTheme.isChecked = it.getBoolean("bLightThemeChecked", binding.bLightTheme.isChecked)
            binding.bDarkTheme.isChecked = it.getBoolean("bDarkThemeChecked", binding.bDarkTheme.isChecked)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("bLightThemeChecked", binding.bLightTheme.isChecked)
        outState.putBoolean("bDarkThemeChecked", binding.bDarkTheme.isChecked)
    }

    fun saveLog(v: View) {
        logServiceHelper.saveLog((v as TextView).text.toString())
    }

    fun readLog() {
        lifecycleScope.launchWhenResumed {
            val text = logServiceHelper.readLog()
            launch(Dispatchers.Main) {
                setTextFromLog(text)
            }
        }
    }

    private fun deleteLog() {
        logServiceHelper.deleteLog()
    }

    private suspend fun setTextFromLog(text: String) {
        val textMetricsParams = TextViewCompat.getTextMetricsParams(binding.tvMainFragmentContainer)
        val precomputedText = withContext(Dispatchers.Default) {
            val htmlText = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)
            PrecomputedTextCompat.create(htmlText, textMetricsParams)
        }
        TextViewCompat.setPrecomputedText(binding.tvMainFragmentContainer, precomputedText)
        delay(100)
        scroll_main_fragment.fullScroll(View.FOCUS_DOWN)
    }


}
