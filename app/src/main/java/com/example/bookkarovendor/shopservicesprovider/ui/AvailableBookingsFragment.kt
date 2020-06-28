package com.example.bookkarovendor.shopservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAvailableShopBinding


class AvailableBookingsFragment : Fragment() {

    private lateinit var viewModel: ShopBookingsViewModel

    private lateinit var binding: FragmentAvailableShopBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, ShopBookingsViewModelFactory(requireActivity().application)).get(
                ShopBookingsViewModel::class.java
            )
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_available_shop,
            container,
            false
        )

        binding.bookingsRecyclerShopAvailable.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getPendingBookings()
            .observe(viewLifecycleOwner, Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptBookingsAdapterShop(
                        bookings,
                        requireContext(),
                        requireActivity().application,
                        viewModel
                    )
                    binding.bookingsRecyclerShopAvailable.adapter = adapter
                } else {
                    setNoBookings()
                }
            })

        return binding.root
    }

    private fun setNoBookings() {
        binding.bookingsRecyclerShopAvailable.visibility = View.GONE
        binding.noBookingsTextShopAvailable.visibility = View.VISIBLE
        binding.noBookingsImageShopAvailable.visibility = View.VISIBLE
    }

    private fun setBookingsExist() {
        binding.bookingsRecyclerShopAvailable.visibility = View.VISIBLE
        binding.noBookingsTextShopAvailable.visibility = View.GONE
        binding.noBookingsImageShopAvailable.visibility = View.GONE
    }

}