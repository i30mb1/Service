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

    private val serviceHelper by lazy { LogServiceHelper(requireActivity()) }
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
        lifecycle.addObserver(serviceHelper)
        serviceHelper.addServerConnectedListener { readLog() }
        binding.bMainFragmentReadLogs.setOnLongClickListener { deleteLog(); readLog(); true }
    }

    fun saveLog(v: View) {
        serviceHelper.saveLog((v as TextView).text.toString())
    }

    fun readLog() {
        lifecycleScope.launchWhenResumed {
            val text = serviceHelper.readLog()
            launch(Dispatchers.Main.immediate) {
                setTextFromLog(text)
            }
        }
    }

    private fun deleteLog() {
        serviceHelper.deleteLog()
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
