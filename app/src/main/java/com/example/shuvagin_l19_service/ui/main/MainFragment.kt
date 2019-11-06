package com.example.shuvagin_l19_service.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shuvagin_l19_service.R
import com.example.shuvagin_l19_service.databinding.MainFragmentBinding
import com.example.shuvagin_l19_service.utils.getColorFromAttr
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
    }

    @SuppressLint("SetTextI18n")
    fun printLog(v: View) {
        if (ll_main_fragment_container.childCount > 8) ll_main_fragment_container.removeAllViews()
        TextView(context).apply {
           text = SpannableStringBuilder().append("PRESSED ")
                .color(requireContext().getColorFromAttr(R.attr.colorPrimary)) { append("\"") }
                .append((v as TextView).text)
                .color(requireContext().getColorFromAttr(R.attr.colorPrimary)) { append("\"") }
            setPadding(10)
            ll_main_fragment_container.addView(this)
        }
    }

    private fun setDefaultValues() {
        viewModel.isLightTheme.value?.let {
            b_dark_theme.isChecked = !it
            b_light_theme.isChecked = it
        }
    }

    fun setLightTheme(lightTheme: Boolean) {
        viewModel.isLightTheme.value = lightTheme
    }

}
