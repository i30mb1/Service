package com.example.shuvagin_l19_service.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.shuvagin_l19_service.R
import com.example.shuvagin_l19_service.databinding.MainFragmentBinding
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
//        setDefaultValues()
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
