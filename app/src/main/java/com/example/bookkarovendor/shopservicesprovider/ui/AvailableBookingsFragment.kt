package com.example.bookkarovendor.shopservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAvailableDeliveryBinding
import com.example.bookkarovendor.databinding.FragmentAvailableShopBinding
import com.example.bookkarovendor.deliveryservicesprovider.ui.DeliveryBookingsViewModel
import com.example.bookkarovendor.deliveryservicesprovider.ui.DeliveryBookingsViewModelFactory


class AvailableBookingsFragment : Fragment() {
    private lateinit var binding: FragmentAvailableShopBinding

    private lateinit var viewModel: ShopBookingsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=
            DataBindingUtil.inflate(inflater,R.layout.fragment_available_shop,container,false)
        viewModel= ViewModelProvider(this,
            ShopBookingsViewModelFactory(requireActivity().application)
        ).get(
            ShopBookingsViewModel::class.java
        )





        return binding.root
    }


}