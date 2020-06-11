package com.example.bookkarovendor.deliveryservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAvailableDeliveryBinding


class AvailableBookingsFragment : Fragment() {
    private lateinit var binding:FragmentAvailableDeliveryBinding

    private lateinit var viewModel: DeliveryBookingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_available_delivery,container,false)
        viewModel=ViewModelProvider(this,DeliveryBookingsViewModelFactory(requireActivity().application)).get(
            DeliveryBookingsViewModel::class.java
        )





        return binding.root
    }


}