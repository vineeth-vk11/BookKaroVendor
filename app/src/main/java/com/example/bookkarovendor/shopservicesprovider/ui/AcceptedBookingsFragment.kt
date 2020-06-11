package com.example.bookkarovendor.shopservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAcceptedShopBinding
import com.example.bookkarovendor.databinding.FragmentDeliveryAcceptedBinding
import com.example.bookkarovendor.deliveryservicesprovider.ui.DeliveryBookingsViewModel
import com.example.bookkarovendor.deliveryservicesprovider.ui.DeliveryBookingsViewModelFactory


class AcceptedBookingsFragment : Fragment() {
    private lateinit var viewModel: ShopBookingsViewModel
    private lateinit var binding: FragmentAcceptedShopBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=
            DataBindingUtil.inflate(inflater,R.layout.fragment_accepted_shop,container,false)
        viewModel =
            ViewModelProvider(this,
                ShopBookingsViewModelFactory(requireActivity().application)
            ).get(
                ShopBookingsViewModel::class.java
            )

        return binding.root
    }


}