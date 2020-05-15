package com.example.bookkarovendor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAllBookingsBinding

class AllBookingsFragment : Fragment() {

    private lateinit var viewModel: FirebaseViewModel

    private lateinit var binding: FragmentAllBookingsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_bookings, container, false)



        return binding.root
    }
}
